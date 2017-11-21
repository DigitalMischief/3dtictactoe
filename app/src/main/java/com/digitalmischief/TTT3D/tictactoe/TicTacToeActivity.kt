package com.digitalmischief.TTT3D.tictactoe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.digitalmischief.TTT3D.R
import com.digitalmischief.TTT3D.models.Board
import com.digitalmischief.TTT3D.util.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import kotlinx.android.synthetic.main.cell_layout.view.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.games.Games
import com.google.android.gms.games.InvitationsClient
import com.google.android.gms.games.TurnBasedMultiplayerClient
import com.google.android.gms.games.multiplayer.Invitation
import com.google.android.gms.games.multiplayer.InvitationCallback
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchUpdateCallback
import com.google.android.gms.tasks.OnSuccessListener


class TicTacToeActivity : AppCompatActivity(), TicTacToeContract.View {
    override var presenter: TicTacToeContract.Presenter = TicTacToePresenter(this)

    private val recyclerAdapter = BoardRecyclerAdapter(presenter)


    val RC_SIGN_IN = 9000
    val RC_SELECT_PLAYERS = 9010
    val RC_FIND_GAME_LOGIN = 9001
    val RC_LOOK_AT_MATCHES = 10000

    private lateinit var turnBasedMultiClient: TurnBasedMultiplayerClient
    private lateinit var invitationsClient: InvitationsClient
    private var match: TurnBasedMatch? = null

    private var displayName: String? = null
    private var playerId: String? = null
    private var opponentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        board_recycler_view.apply{
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(context, 3)
        }

        board_recycler_view.visibility = View.INVISIBLE


        setSignInOutButton()

        setOnClickListners()

        presenter.onStart()

    }

    override fun showBoard(board: Board) {
        board_recycler_view.layoutManager =
                GridLayoutManager(this, presenter.getSpanSize())
        refreshBoard()
    }

    override fun refreshBoard() {
        recyclerAdapter.notifyDataSetChanged()
    }

    override fun setCellType( index: Int, type: String ){
        board_recycler_view.layoutManager.findViewByPosition(index).cell_type_text.text = type
    }

    override fun showPlayerFault(){
        Utils.showMessageShort(this,"watch doin checker?")
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

    override fun takeTurn(){

        match?.let{
            turnBasedMultiClient.takeTurn(it.matchId,
                    presenter.serializeBoard(), opponentId)
            updateMatch(it)
        }

    }

    override fun isMyTurn(): Boolean{
        val turnStatus = match?.turnStatus

        when (turnStatus) {
            TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN -> return true

            TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN -> {
                Utils.showMessageShort(this, "It is their turn")
                return false
            }

            TurnBasedMatch.MATCH_TURN_STATUS_INVITED ->{
                Utils.showMessageShort(this, "Still waiting for invitations")
                return false
            }
            else -> {
                Utils.showMessageShort(this, "Unknown Error")
                return false
            }
        }
    }

    fun updateMatch(match: TurnBasedMatch) {
        this.match = match

        val status = match.status
        val turnStatus = match.turnStatus

        when (status) {
            TurnBasedMatch.MATCH_STATUS_CANCELED ->
                Utils.showMessageShort(this, "Match Cancelled")
            TurnBasedMatch.MATCH_STATUS_EXPIRED ->
                Utils.showMessageShort(this, "Message Expired")
            TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING ->
                Utils.showMessageShort(this, "Waiting for an automatch partner")
            TurnBasedMatch.MATCH_STATUS_COMPLETE ->
                Utils.showMessageShort(this, "Game is complete")

        }

        when (turnStatus) {
            TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN -> {
                board_recycler_view.visibility = View.VISIBLE
                presenter.deserializeBoard(match.data)
                refreshBoard()
            }

            TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN ->
                Utils.showMessageShort(this, "It is their turn")

            TurnBasedMatch.MATCH_TURN_STATUS_INVITED ->
                Utils.showMessageShort(this, "Still waiting for invitations")
        }

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


    fun onInitiateMatch(match: TurnBasedMatch?) {
        match?.data?.let {
            updateMatch(match)
            opponentId = match.pendingParticipantId
            return
        }

        startMatch(match)

    }

    fun startMatch(match: TurnBasedMatch?) {

        match?.let {

            this.match = it

            val myParticipantId = it.getParticipantId(playerId)

            turnBasedMultiClient.takeTurn(it.matchId, presenter.serializeBoard(), myParticipantId)
                    .addOnSuccessListener { updateMatch(it) }
        }
    }

    private val invitationCallback = object : InvitationCallback() {
        override fun onInvitationRemoved(p0: String) {
            Utils.showMessageShort(this@TicTacToeActivity, "An invitation was removed.")
        }

        // Handle notification events.
        override fun onInvitationReceived(invitation: Invitation) {
            Utils.showMessageShort(this@TicTacToeActivity,
                    "An invitation has arrived from " + invitation.inviter.displayName)

        }
    }

    private val matchUpdateCallback = object : TurnBasedMatchUpdateCallback() {
        override fun onTurnBasedMatchReceived(turnBasedMatch: TurnBasedMatch) {
            Utils.showMessageShort(this@TicTacToeActivity, "A match was updated.")
        }

        override fun onTurnBasedMatchRemoved(matchId: String) {
            Utils.showMessageShort(this@TicTacToeActivity, "A match was removed.")
        }
    }
}