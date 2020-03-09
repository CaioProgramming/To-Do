package com.myself.todo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dx.dxloadingbutton.lib.LoadingButton
import com.github.mmin18.widget.RealtimeBlurView
import com.google.firebase.auth.FirebaseAuth
import com.myself.todo.Beans.User
import com.myself.todo.Database.DadosOpenHelper
import com.myself.todo.Database.UserRepository
import com.myself.todo.Utils.Utilities
import de.hdodenhof.circleimageview.CircleImageView
import de.mateware.snacky.Snacky
import org.junit.runner.RunWith

class Login : AppCompatActivity() {
    private var conexao: SQLiteDatabase? = null
    private var dadosOpenHelper: DadosOpenHelper? = null
    var entrar: LoadingButton? = null
    var userinput: EditText? = null
    var pass: EditText? = null
    var usuarioB: User? = null
    var profilepic: CircleImageView? = null
    private var usuarioRepositorio: UserRepository? = null
    private var title: TextView? = null
    private var form: LinearLayout? = null
    private var btnlogin: LoadingButton? = null
    private var useregister: EditText? = null
    private var passreg: EditText? = null
    private var formregister: LinearLayout? = null
    private var registerform: RelativeLayout? = null
    private var reg: LoadingButton? = null
    var user = FirebaseAuth.getInstance().currentUser
    private var mAuth: FirebaseAuth? = null
    private var welcomeblur: RealtimeBlurView? = null
    private var usernamelogin: TextView? = null
    private var formregbtn: Button? = null
    private var profileback: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        profileback = findViewById<ImageView?>(R.id.profileback)
        formregbtn = findViewById<Button?>(R.id.formregbtn)
        usernamelogin = findViewById<TextView?>(R.id.usernamelogin)
        welcomeblur = findViewById<RealtimeBlurView?>(R.id.welcomeblur)
        mAuth = FirebaseAuth.getInstance()
        reg = findViewById<LoadingButton?>(R.id.reg)
        registerform = findViewById<RelativeLayout?>(R.id.registerform)
        formregister = findViewById<LinearLayout?>(R.id.formregister)
        passreg = findViewById<EditText?>(R.id.passreg)
        useregister = findViewById<EditText?>(R.id.useregister)
        btnlogin = findViewById<LoadingButton?>(R.id.btnlogin)
        form = findViewById<LinearLayout?>(R.id.form)
        title = findViewById<TextView?>(R.id.title)
        usuarioB = User()
        userinput = findViewById<EditText?>(R.id.user)
        pass = findViewById<EditText?>(R.id.pass)
        val title = findViewById<TextView?>(R.id.title)
        profilepic = findViewById<CircleImageView?>(R.id.profile_pic)
        entrar = findViewById<LoadingButton?>(R.id.btnlogin)
        val Atelas = Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Regular.ttf")
        title.setTypeface(Atelas)
        userinput.setTypeface(Atelas)
        pass.setOnEditorActionListener(OnEditorActionListener { textView, i, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || i == EditorInfo.IME_ACTION_DONE) {
                validarLogin()
            }
            false
        })
        criarConexao()
        checkPermissionREAD_EXTERNAL_STORAGE(this)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.getCurrentUser()
        if (currentUser != null && currentUser.email != null) {
            begin()
        }
        //begin();
    }

    private fun criarConexao() {
        dadosOpenHelper = DadosOpenHelper(this)
        conexao = dadosOpenHelper.getWritableDatabase()
        usuarioRepositorio = UserRepository(conexao)
        //Toast.makeText(this,"CONEXÃO CRIADA COM SUCESSO!", Toast.LENGTH_SHORT).show();
    }

    fun MessageRegister() {
        Snacky.builder()
                .setActivity(this)
                .setText("Cadastro concluído")
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show()
    }

    fun register(view: View?) {
        val myanim2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
        registerform.setVisibility(View.VISIBLE)
        registerform.startAnimation(myanim2)
    }

    fun begin() {
        form.setVisibility(View.INVISIBLE)
        btnlogin.setVisibility(View.INVISIBLE)
        reg.setVisibility(View.INVISIBLE)
        formregbtn.setVisibility(View.INVISIBLE)
        welcomeblur.setVisibility(View.VISIBLE)
        Glide.with(this).load(user.getPhotoUrl()).into(profileback)
        usernamelogin.setText("Olá " + if (user != null) user.getDisplayName() else null)
        val `in` = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        welcomeblur.startAnimation(`in`)
        profileback.startAnimation(`in`)
        usernamelogin.startAnimation(`in`)
        val countDownTimer = object : CountDownTimer(5000, 300) {
            override fun onTick(l: Long) {}
            override fun onFinish() {
                StartApp()
            }
        }.start()
    }

    fun StartApp() {
        val i = Intent(this, WelcomeActivity::class.java)
        val i2 = Intent(this, Mylist::class.java)
        if (user.getDisplayName() == null || user.getDisplayName() == "") {
            startActivity(i)
            finish()
        } else {
            startActivity(i2)
            finish()
        }
    }

    fun userDefined(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        val usuario = false
        return if (user != null) {
            user.displayName != null
        } else {
            false
        }
    }

    fun checkPermissionREAD_EXTERNAL_STORAGE(
            context: Context?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity?,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("Armazenamento externo", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    ActivityCompat
                            .requestPermissions(
                                    context as Activity?, arrayOf<String?>(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    Mylist.Companion.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    fun showDialog(msg: String?, context: Context?,
                   permission: String?) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permissão")
        alertBuilder.setMessage("$msg permissão necessária")
        alertBuilder.setPositiveButton(android.R.string.yes
        ) { dialog, which ->
            ActivityCompat.requestPermissions(context as Activity?, arrayOf(permission),
                    Mylist.Companion.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    fun MessageError(mensagem: String?) {
        Snacky.builder()
                .setActivity(this)
                .setText(mensagem)
                .setDuration(Snacky.LENGTH_SHORT)
                .error()
                .show()
    }

    fun validarLogin() {
        val usuario = userinput.getText().toString()
        val senha = pass.getText().toString()
        val myanim2 = AnimationUtils.loadAnimation(this, R.anim.popin)
        entrar.startLoading()
        try {
            val isValid = usuarioRepositorio.validaLogin(usuario, senha)
            if (isValid) {
                usuarioRepositorio.findByLogin(usuario, senha)
                usuarioB = usuarioRepositorio.findByLogin(usuario, senha)
                println(usuarioB.getProfilepic())
                if (usuarioB.getProfilepic() == null) {
                    Snacky.builder()
                            .setActivity(this)
                            .setText("Sem foto de perfil " + usuarioB.getUser())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show()
                    val counter: CountDownTimer = object : CountDownTimer(2000, 100) {
                        override fun onTick(l: Long) {
                            entrar.loadingSuccessful()
                        }

                        override fun onFinish() {
                            begin()
                        }
                    }
                    counter.start()
                } else {
                    Snacky.builder()
                            .setActivity(this)
                            .setText("Bem-vindo " + usuarioB.getUser())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show()
                    profilepic.setVisibility(View.VISIBLE)
                    profilepic.setAnimation(myanim2)
                    profilepic.setImageBitmap(Utilities.Companion.handleSamplingAndRotationBitmap(this, Uri.parse(usuarioB.getProfilepic())))
                    val counter: CountDownTimer = object : CountDownTimer(2000, 100) {
                        override fun onTick(l: Long) {
                            entrar.loadingSuccessful()
                        }

                        override fun onFinish() {
                            begin()
                        }
                    }
                    counter.start()
                }
                //Toast.makeText(this,"Usuario e senha validados com sucesso!",Toast.LENGTH_SHORT).show();
            } else {
                MessageError("Usuário e/ou senha incorretos")
                val counter: CountDownTimer = object : CountDownTimer(2000, 100) {
                    override fun onTick(l: Long) {
                        entrar.loadingFailed()
                    }

                    override fun onFinish() {
                        userinput.setFocusable(true)
                        entrar.reset()
                    }
                }
                counter.start()
            }
        } catch (e: Exception) {
            MessageError("Erro ao fazer login $e")
            e.printStackTrace()
        }
    }

    private fun LoginSucess(usuario: String?) {
        Snacky.builder()
                .setActivity(this)
                .success()
                .setText("Bem-vindo $usuario")
                .show()
    }

    fun login(view: View?) {
        mAuth.signInWithEmailAndPassword(userinput.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.getCurrentUser()
                        LoginSucess(user.getDisplayName())
                        SetProfilePic(user.getPhotoUrl())
                        begin()
                    } else {
                        MessageError("Erro ao fazer login " + task.exception)
                    }
                }
        validarLogin()
    }

    fun SetProfilePic(uri: Uri?) {
        Glide.with(this).load(uri).into(profilepic)
    }

    fun cadastrar(view: View?) {
        reg.startLoading()
        val registerform = registerform
        val loadingButton = reg
        criarConexao()
        val usuario = User()
        usuario.user = useregister.getText().toString()
        usuario.password = passreg.getText().toString()
        mAuth.createUserWithEmailAndPassword(usuario.user, usuario.password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                loadingButton.loadingSuccessful()
                val user = mAuth.getCurrentUser()
                usuarioB.setUser(user.getDisplayName())
                usuarioB.setProfilepic(user.getPhotoUrl().toString())
                begin()
                MessageRegister()
            } else {
                MessageError("Erro ao cadastrar " + task.exception)
            }
        }
        MessageRegister()
        val out = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        userinput.setText(usuario.user)
        pass.setText(usuario.password)
        registerform.startAnimation(out)
        val countDownTimer = object : CountDownTimer(3000, 100) {
            override fun onTick(l: Long) {
                loadingButton.loadingSuccessful()
            }

            override fun onFinish() {
                registerform.setVisibility(View.INVISIBLE)
            }
        }.start()
    }
}