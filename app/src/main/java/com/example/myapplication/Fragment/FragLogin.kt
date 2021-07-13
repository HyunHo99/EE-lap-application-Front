package com.example.myapplication.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import com.example.myapplication.activity.MyGlobal.Companion.globalVar
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener


class FragLogin : Fragment() {
    val TAG: String = "로그"
    val RC_SIGN_IN=1000

    lateinit var userNameText : TextView
    lateinit var userEmailText : TextView
    lateinit var userPhoto : ImageView
    lateinit var signinBt : View
    lateinit var signoutBt : View
    lateinit var card : View
    lateinit var mGoogleSignInClient : GoogleSignInClient

    object ImageLoder{
        suspend fun loadImage(imageUrl:String) : Bitmap?{
            val bmp:Bitmap?=null
            try{
                val url = URL(imageUrl)
                val stream = url.openStream()
                return BitmapFactory.decodeStream(stream)
            }catch (e:MalformedURLException){
                e.printStackTrace()
            } catch (e:IOException){
                e.printStackTrace()
            }
            return bmp
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userNameText = view.findViewById(R.id.txt_userName)
        userEmailText = view.findViewById(R.id.txt_userEmail)
        userPhoto = view.findViewById(R.id.img_user)
        signinBt = view.findViewById(R.id.bt_signin)
        signoutBt = view.findViewById(R.id.bt_signout)
        card = view.findViewById(R.id.Login_cardView)
        val gso : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestEmail().build()
        val gsa = GoogleSignIn.getLastSignedInAccount(requireActivity())
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        if(gsa==null) {
            signinBt.visibility = View.VISIBLE
            signinBt.setOnClickListener { v ->
                val intent = mGoogleSignInClient.signInIntent
                signIn(intent)
            }
        }
        else{
            requireActivity().runOnUiThread {
                userNameText.text = gsa.displayName
                userEmailText.text = gsa.email
                globalVar = gsa.id ?: "0"
            }
            setUserDataVisible()
            if(gsa.photoUrl!=null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        ImageLoder.loadImage(gsa.photoUrl.toString())
                    }
                    userPhoto.setImageBitmap(bitmap)
                }
            }
            else{
                userPhoto.setImageBitmap(BitmapFactory.decodeResource(resources, R.mipmap.ic_profile))
            }
        }
    }

    private fun signOut(mGoogleSignInClient: GoogleSignInClient) {
        mGoogleSignInClient.signOut()
        userEmailText.visibility = View.GONE
        userNameText.visibility = View.GONE
        userPhoto.visibility = View.GONE
        signoutBt.visibility = View.GONE
        card.visibility = View.GONE
        signinBt.visibility = View.VISIBLE
        globalVar = "0"
        signinBt.setOnClickListener { v ->
            val intent = mGoogleSignInClient.signInIntent
            signIn(intent)
        }
    }


    private fun signIn(intent: Intent){
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    @SuppressLint("SetTextI18n")
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto: Uri? = acct.photoUrl
                requireActivity().runOnUiThread {
                    userNameText.text = personName
                    userEmailText.text = personEmail
                    globalVar = personId ?: "0"
                }
                setUserDataVisible()

                if(personPhoto!=null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val bitmap = withContext(Dispatchers.IO) {
                            ImageLoder.loadImage(personPhoto.toString())
                        }
                        userPhoto.setImageBitmap(bitmap)
                    }
                }
                else{
                    userPhoto.setImageBitmap(BitmapFactory.decodeResource(resources, R.mipmap.ic_profile))
                }
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==RC_SIGN_IN){
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)

        }
    }
    private fun setUserDataVisible(){
        requireActivity().runOnUiThread{
            userNameText.visibility=View.VISIBLE
            userEmailText.visibility=View.VISIBLE
            userPhoto.visibility = View.VISIBLE
            signinBt.visibility=View.INVISIBLE
            signoutBt.visibility = View.VISIBLE
            card.visibility = View.VISIBLE
            signoutBt.setOnClickListener { v->
                signOut(mGoogleSignInClient)
            }
        }

    }
}