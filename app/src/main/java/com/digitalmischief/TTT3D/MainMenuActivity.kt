package com.digitalmischief.TTT3D

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_main_menu.*
import com.google.android.gms.games.Games
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import android.app.Activity
import android.app.ProgressDialog.show
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.digitalmischief.TTT3D.util.Utils
import com.google.android.gms.games.TurnBasedMultiplayerClient
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.games.InvitationsClient
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch.*
import com.google.android.gms.tasks.OnSuccessListener
import android.widget.Toast
import android.support.annotation.NonNull
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchUpdateCallback
import com.google.android.gms.games.multiplayer.Invitation
import com.google.android.gms.games.multiplayer.InvitationCallback




class MainMenuActivity : AppCompatActivity() {

    val RC_SIGN_IN = 9000
    val RC_SELECT_PLAYERS = 9010
    val RC_FIND_GAME_LOGIN = 9001
    val RC_LOOK_AT_MATCHES = 10000

    private lateinit var turnBasedMultiClient: TurnBasedMultiplayerClient
    private lateinit var invitationsClient: InvitationsClient
    private var match: TurnBasedMatch? = null
    private var turnData: TicTacToeTurn? = null

    private var displayName: String? = null
    private var playerId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        setSignInOutButton()

        setOnClickListners()

    }

    fun setOnClickListners() {
        val onClickListener = View.OnClickListener {
            when (it) {
                signin_button -> startSignInIntent(RC_SIGN_IN)
                signout_button -> signOut()
                find_game_button -> findGame()
                check_games_button -> checkGames()
            }
        }

        signin_button.setOnClickListener(onClickListener)
        signout_button.setOnClickListener(onClickListener)
        find_game_button.setOnClickListener(onClickListener)
        check_games_button.setOnClickListener(onClickListener)
    }

    override fun onResume() {
        super.onResume()
        signInSilently()
    }

    fun setSignInOutButton() {
        if (isSignedIn()) {
            showSignOutButton()
        } else {
            showSignInButton()
        }
    }


    fun showSignInButton() {
        signout_button.visibility = View.GONE
        signin_button.visibility = View.VISIBLE
    }

    fun showSignOutButton() {
        signout_button.visibility = View.VISIBLE
        signin_button.visibility = View.GONE
    }

    fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null
    }


    fun checkGames() {
        turnBasedMultiClient.inboxIntent.addOnSuccessListener(object : OnSuccessListener<Intent> {
            override fun onSuccess(intent: Intent?) {
                startActivityForResult(intent, RC_LOOK_AT_MATCHES)
            }

        })

    }

    fun findGame() {
        if (isSignedIn()) {
            val allowAutoMatch = false
            Games.getTurnBasedMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
                    .getSelectOpponentsIntent(1, 1, allowAutoMatch)
                    .addOnSuccessListener { intent -> startActivityForResult(intent, RC_SELECT_PLAYERS) }
        } else {
            startSignInIntent(RC_FIND_GAME_LOGIN)
        }
    }


    fun signOut() {
        val signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
        signInClient.signOut().addOnCompleteListener(this
        ) {
            showSignInButton()
        }
    }

    fun startSignInIntent(requestCode: Int) {

        val builder: GoogleSignInOptions = GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestEmail()
                .build()

        val signInClient = GoogleSignIn.getClient(this, builder)

        startActivityForResult(signInClient.signInIntent, requestCode)
    }

    fun signInSilently() {
        val signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
        signInClient.silentSignIn().addOnCompleteListener(this
        ) { task ->
            if (task.isSuccessful) {
                val signedInAccount = task.result
                onConnected(signedInAccount)
            } else {
                startSignInIntent(RC_SIGN_IN)
            }
        }
    }

    private fun onConnected(googleSignInAccount: GoogleSignInAccount?) {

        googleSignInAccount?.let {

            turnBasedMultiClient = Games.getTurnBasedMultiplayerClient(this, it)
            invitationsClient = Games.getInvitationsClient(this, it)

            Games.getPlayersClient(this, it)
                    .currentPlayer
                    .addOnSuccessListener { player ->
                        displayName = player.displayName
                        playerId = player.playerId
                    }


            // Retrieve the TurnBasedMatch from the connectionHint
            val gamesClient = Games.getGamesClient(this, it)

            gamesClient.activationHint
                    .addOnSuccessListener { hint ->
                        if (hint != null) {
                            val match = hint.getParcelable<TurnBasedMatch>(Multiplayer.EXTRA_TURN_BASED_MATCH)
                            if (match != null) {
                                updateMatch(match)
                            }
                        }
                    }
        } ?: Utils.showMessageShort(this, "No use logged in")

    }

    fun getNextParticipantId(): String? {


        val myParticipantId = match?.getParticipantId(playerId)

        val participantIds = match?.participantIds

        var desiredIndex = -1
        participantIds?.let { pids ->
            for (i in 0 until pids.size) {
                if (pids[i] == myParticipantId)
                    desiredIndex = i + 1
            }

            if (desiredIndex < pids.size)
                return pids[desiredIndex]

            match?.let { m ->
                if (m.availableAutoMatchSlots <= 0)
                    return pids[0]
                else
                    return null
            }

        }

        return null
    }

    fun updateMatch(match: TurnBasedMatch) {
        this.match = match

        val status = match.status
        val turnStatus = match.turnStatus

        when (status) {
            MATCH_STATUS_CANCELED ->
                Utils.showMessageShort(this, "Match Cancelled")
            MATCH_STATUS_EXPIRED ->
                Utils.showMessageShort(this, "Message Expired")
            MATCH_STATUS_AUTO_MATCHING ->
                Utils.showMessageShort(this, "Waiting for an automatch partner")
            MATCH_STATUS_COMPLETE ->
                Utils.showMessageShort(this, "Game is complete")

        }

        when (turnStatus) {
            MATCH_TURN_STATUS_MY_TURN ->
                turnData //todo: need to implement turn class

            MATCH_TURN_STATUS_THEIR_TURN ->
                Utils.showMessageShort(this, "It is their turn")

            MATCH_TURN_STATUS_INVITED ->
                Utils.showMessageShort(this, "Still waiting for invitations")
        }

        turnData = null

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN || requestCode == RC_FIND_GAME_LOGIN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // The signed in account is stored in the result.
                val signedInAccount = result.signInAccount
                showSignOutButton()

                onConnected(signedInAccount)

                if (requestCode == RC_FIND_GAME_LOGIN) findGame()

            } else {
                var message = result.status.statusMessage
                if (message == null || message.isEmpty()) {
                    message = "Unknown Error"
                }
                AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show()
            }
        } else if (requestCode == RC_LOOK_AT_MATCHES) {
            // Returning from the 'Select Match' dialog

            if (resultCode != Activity.RESULT_OK) {
                //logBadActivityResult(requestCode, resultCode, "User cancelled returning from the 'Select Match' dialog.")
                return
            }

            val match: TurnBasedMatch? = data?.getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH)

            if (match != null) {
                updateMatch(match)
            }

        } else if (requestCode == RC_SELECT_PLAYERS) {
            if (resultCode != Activity.RESULT_OK) {
                // Canceled or other unrecoverable error.
                return
            }


            val invitees = data?.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS)

            val builder = TurnBasedMatchConfig.builder()
                    .addInvitedPlayers(invitees).build()

            val lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this)

            turnBasedMultiClient.createMatch(builder)
                    .addOnSuccessListener(object : OnSuccessListener<TurnBasedMatch> {
                        override fun onSuccess(match: TurnBasedMatch?) {
                            onInitiateMatch(match)
                        }

                    })
        }
    }

    fun takeTurn(){


    }

    fun onInitiateMatch(match: TurnBasedMatch?) {
        match?.data?.let {
            updateMatch(match)
            return
        }

        startMatch(match)

    }

    fun startMatch(match: TurnBasedMatch?) {
        turnData = TicTacToeTurn()
        turnData!!.data = "First Turn"

        match?.let {

            this.match = it

            val myParticipantId = it.getParticipantId(playerId)


            turnBasedMultiClient.takeTurn(it.matchId, turnData?.persist(), myParticipantId)
                    .addOnSuccessListener { updateMatch(it) }
        }
    }

    private val invitationCallback = object : InvitationCallback() {
        override fun onInvitationRemoved(p0: String) {
            Utils.showMessageShort(this@MainMenuActivity, "An invitation was removed.")
        }

        // Handle notification events.
        override fun onInvitationReceived(invitation: Invitation) {
            Utils.showMessageShort(this@MainMenuActivity,
                    "An invitation has arrived from " + invitation.inviter.displayName)

        }
    }

    private val matchUpdateCallback = object : TurnBasedMatchUpdateCallback() {
        override fun onTurnBasedMatchReceived(turnBasedMatch: TurnBasedMatch) {
            Utils.showMessageShort(this@MainMenuActivity, "A match was updated.")
        }

        override fun onTurnBasedMatchRemoved(matchId: String) {
            Utils.showMessageShort(this@MainMenuActivity, "A match was removed.")
        }
    }
}