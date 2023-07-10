package com.example.abithshiksha.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.abithshiksha.MainActivity
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentRegistrationBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.SelectSubjectAdapter
import com.example.abithshiksha.view_model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.user.caregiver.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val mSignUpViewModel: SignUpViewModel by viewModel()
    private val mSendNumberViewModel: SendNumberViewModel by viewModel()
    private val mVerifyMobileOtpViewModel: VerifyMobileOtpViewModel by viewModel()
    private val mSendEmailOtpViewModel: SendEmailOtpViewModel by viewModel()
    private val mVerifyEmailViewModel: VerifyEmailViewModel by viewModel()
    private var emailVerify: String = ""
    private var mobileVerify: String = ""
    private lateinit var accessToken: String
    var cTimer: CountDownTimer? = null
    private var above18: Boolean = false
    private var is_above18: Int? = null

    //board class
    var classList: MutableList<String> = mutableListOf()
    var classIdList: MutableList<Int> = mutableListOf()

    var boardList: MutableList<String> =  mutableListOf()
    var boardIdList: MutableList<Int> =  mutableListOf()

    private var class_global: String = ""
    private var std: Int = 0
    private var board = ""
    private var boardId: Int = 0
    private val mGetBoardsViewModel: GetBoardsViewModel by viewModel()
    private val mGetAllClassViewModel: GetAllClassViewModel by viewModel()

    private lateinit var emailOtpDialog: BottomSheetDialog
    private lateinit var numberOtpDialog: BottomSheetDialog
    private lateinit var e_edTxt1: EditText
    private lateinit var e_edTxt2: EditText
    private lateinit var e_edTxt3: EditText
    private lateinit var e_edTxt4: EditText
    private lateinit var e_edTxt5: EditText
    private lateinit var e_edTxt6: EditText
    private lateinit var m_edTxt1: EditText
    private lateinit var m_edTxt2: EditText
    private lateinit var m_edTxt3: EditText
    private lateinit var m_edTxt4: EditText
    private lateinit var m_edTxt5: EditText
    private lateinit var m_edTxt6: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        emailFocusListener()
        mobileFocusListener()
        passwordFocusListener()
        conPasswordFocusListener()
        nameFocusListener()
        parentsNameFocusListener()
        binding.verifyEmailHtv.gone()
        binding.verifyMobileHtv.gone()
        binding.progressBar.gone()
        binding.parentsNameTxtLay.gone()
        binding.classSpinnerLay.gone()


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()

        binding.loginNowTv.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.verifyEmailHtv.setOnClickListener {
            if(requireActivity().isConnectedToInternet()){
                sendEmailOtp(binding.emailTxt.text.toString(),accessToken,false)
            }else{
                Toast.makeText(requireActivity(),"No internet connection.",Toast.LENGTH_SHORT).show()
            }
        }

        binding.verifyMobileHtv.setOnClickListener {
            requireActivity().hideSoftKeyboard()
            if(requireActivity().isConnectedToInternet()){
                sendNumberOtp(accessToken, binding.mobileTxt.text.toString(),false)
            }else{
                Toast.makeText(requireActivity(),"No internet connection.",Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupBtn.setOnClickListener {
            submitForm()
        }

        showAgePopUp()

        //board class
        setupClassSpinner()
        getBoards()
    }

    private fun submitForm() {
        val validParentsName = binding.parentsNameTxtLay.helperText == null
        val validName = binding.nameTxtLay.helperText == null
        val validEmail = binding.emailTxtLay.helperText == null
        val validNumber = binding.mobileTxtLay.helperText == null
        val validPassword = binding.passwordTxtLay.helperText == null
        val validConPassword = binding.conPasswordTxtLay.helperText == null

        if(above18 == true){
            if(validName){
                if(boardId != 0){
                    if(std != 0){
                        if(validEmail){
                            if(validNumber){
                                if(validPassword){
                                    if(validConPassword){
                                        if(binding.passwordTxt.text.toString() == binding.conPasswordTxt.text.toString()){
                                            requireActivity().hideSoftKeyboard()
                                            if(requireActivity().isConnectedToInternet()){
                                                signUp(
                                                    binding.nameTxt.text.toString(),
                                                    binding.emailTxt.text.toString(),
                                                    binding.mobileTxt.text.toString(),
                                                    binding.passwordTxt.text.toString(),
                                                    is_above18!!,
                                                    std,
                                                    boardId,
                                                    null,
                                                )
                                            }else{
                                                Toast.makeText(requireActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show()
                                            }
                                        }else{
                                            Toast.makeText(requireActivity(),"Confirm password mismatch with the new password.", Toast.LENGTH_SHORT).show()
                                            binding.conPasswordTxt.showKeyboard()
                                        }
                                    }else{
                                        Toast.makeText(requireActivity(),binding.conPasswordTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                                        binding.conPasswordTxt.showKeyboard()
                                    }
                                }else{
                                    Toast.makeText(requireActivity(),binding.passwordTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                                    binding.passwordTxt.showKeyboard()
                                }
                            }else{
                                Toast.makeText(requireActivity(),binding.mobileTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                                binding.mobileTxt.showKeyboard()
                            }
                        }else{
                            Toast.makeText(requireActivity(),binding.emailTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                            binding.emailTxt.showKeyboard()
                        }
                    }else{
                        Toast.makeText(requireActivity(),"Please select a class",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireActivity(),"Please select a board",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireActivity(),binding.nameTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                binding.nameTxt.showKeyboard()
            }
        }else{
            if(validParentsName){
                if(validName){
                    if(boardId != 0){
                        if(std != 0){
                            if(validEmail){
                                if(validNumber){
                                    if(validPassword){
                                        if(validConPassword){
                                            if(binding.passwordTxt.text.toString() == binding.conPasswordTxt.text.toString()){
                                                requireActivity().hideSoftKeyboard()
                                                if(requireActivity().isConnectedToInternet()){
                                                    signUp(
                                                        binding.nameTxt.text.toString(),
                                                        binding.emailTxt.text.toString(),
                                                        binding.mobileTxt.text.toString(),
                                                        binding.passwordTxt.text.toString(),
                                                        is_above18!!,
                                                        std,
                                                        boardId,
                                                        binding.parentsNameTxt.text.toString(),
                                                    )
                                                }else{
                                                    Toast.makeText(requireActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show()
                                                }
                                            }else{
                                                Toast.makeText(requireActivity(),"Confirm password mismatch with the new password.", Toast.LENGTH_SHORT).show()
                                                binding.conPasswordTxt.showKeyboard()
                                            }
                                        }else{
                                            Toast.makeText(requireActivity(),binding.conPasswordTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                                            binding.conPasswordTxt.showKeyboard()
                                        }
                                    }else{
                                        Toast.makeText(requireActivity(),binding.passwordTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                                        binding.passwordTxt.showKeyboard()
                                    }
                                }else{
                                    Toast.makeText(requireActivity(),binding.mobileTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                                    binding.mobileTxt.showKeyboard()
                                }
                            }else{
                                Toast.makeText(requireActivity(),binding.emailTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                                binding.emailTxt.showKeyboard()
                            }
                        }else{
                            Toast.makeText(requireActivity(),"Please select a class",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireActivity(),"Please select a board",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireActivity(),binding.nameTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                    binding.nameTxt.showKeyboard()
                }
            }else{
                Toast.makeText(requireActivity(),binding.parentsNameTxtLay.helperText.toString(),Toast.LENGTH_SHORT).show()
                binding.parentsNameTxt.showKeyboard()
            }

        }

    }

    private fun nameFocusListener(){
        binding.nameTxt.doOnTextChanged { text, start, before, count ->
            binding.nameTxtLay.helperText = validName()
        }
    }

    private fun validName(): String? {
        val nameText = binding.nameTxt.text.toString()

        if(nameText.isEmpty()){
            return "Provide your name."
        }
        return null
    }

    private fun parentsNameFocusListener(){
        binding.parentsNameTxt.doOnTextChanged { text, start, before, count ->
            binding.parentsNameTxtLay.helperText = validParentsName()
        }
    }

    private fun validParentsName(): String? {
        val nameText = binding.parentsNameTxt.text.toString()

        if(nameText.isEmpty()){
            return "Provide your parent's name."
        }
        return null
    }

    private fun emailFocusListener(){
        binding.emailTxt.doOnTextChanged { text, start, before, count ->
            binding.emailTxtLay.helperText = validEmail()
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailTxt.text.toString()

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            binding.verifyEmailHtv.gone()
            binding.emailTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            return "Invalid Email Address"
        }else{
            if(emailVerify != binding.emailTxt.text.toString()){
                binding.verifyEmailHtv.visible()
                binding.emailTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }else{
                binding.verifyEmailHtv.gone()
                binding.emailTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_check_24), null)
            }
        }
        return null
    }

    private fun mobileFocusListener(){
        binding.mobileTxt.doOnTextChanged { text, start, before, count ->
            binding.mobileTxtLay.helperText = validMobileNumber()
        }
    }

    private fun validMobileNumber(): String? {
        val mobileText = binding.mobileTxt.text.toString()
        if(mobileText.length != 10){
            binding.verifyMobileHtv.gone()
            binding.mobileTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            return "10 digit number required."
        }else{
            if(mobileVerify != binding.mobileTxt.text.toString()){

                if(binding.mobileTxt.text.toString().toDouble() == 0.00){
                    return "Please provide a valid phone number."
                }else{
                    binding.verifyMobileHtv.visible()
                    binding.mobileTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                }
            }else{
                binding.verifyMobileHtv.gone()
                binding.mobileTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_check_24), null)
            }
        }

        if(binding.mobileTxt.text.toString().toDouble() == 0.00){
            return "Please provide a valid phone number."
        }

        return  null
    }

    private fun passwordFocusListener() {
        binding.passwordTxt.doOnTextChanged { text, start, before, count ->
            binding.passwordTxtLay.helperText = validPassword()
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordTxt.text.toString()
        if(passwordText.length < 6){
            return "Minimum 6 characters required."
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex())){
            return "Must contain 1 upper case character."
        }
        if(!passwordText.matches(".*[a-z].*".toRegex())){
            return "Must contain 1 lower case character."
        }
        if(!passwordText.matches(".*[0-9].*".toRegex())){
            return "Must contain at least 1 number."
        }
        if(!passwordText.matches(".*[!@$#%&*_-].*".toRegex())){
            return "Must contain 1 special character (!@$#%&*_-)."
        }
        return  null
    }


    private fun conPasswordFocusListener() {
        binding.conPasswordTxt.doOnTextChanged { text, start, before, count ->
            binding.conPasswordTxtLay.helperText = validConPassword()
        }
    }
    private fun validConPassword(): String? {
        val new_passwordText = binding.passwordTxt.text.toString()
        val con_passwordText = binding.conPasswordTxt.text.toString()

        if(con_passwordText != new_passwordText){
            return "Password mismatch with the new password."
        }
        return  null
    }

    private fun emailVerifyBottomSheet() {
        emailOtpDialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.otp_bottomsheet_lay, null)
        val btnContinue = view.findViewById<TextView>(R.id.verify_otp_btn)
        val header = view.findViewById<TextView>(R.id.header_txt_1)
        val resend = view.findViewById<TextView>(R.id.resend_tv)
        val timerTv = view.findViewById<TextView>(R.id.timer_tv)

        resend.gone()
        startTimer(timerTv,resend)
        e_edTxt1 = view.findViewById<EditText>(R.id.ed_txt1)
        e_edTxt2 = view.findViewById<EditText>(R.id.ed_txt2)
        e_edTxt3 = view.findViewById<EditText>(R.id.ed_txt3)
        e_edTxt4 = view.findViewById<EditText>(R.id.ed_txt4)
        e_edTxt5 = view.findViewById<EditText>(R.id.ed_txt5)
        e_edTxt6 = view.findViewById<EditText>(R.id.ed_txt6)

        resend.setOnClickListener {
            startTimer(timerTv,resend)
            resend.gone()
            sendEmailOtp(binding.emailTxt.text.toString(),accessToken,true)
        }

        e_edTxt1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(e_edTxt1.text.length == 1){
                    e_edTxt2.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        e_edTxt2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(e_edTxt2.text.length == 1){
                    e_edTxt3.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        e_edTxt3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(e_edTxt3.text.length == 1){
                    e_edTxt4.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        e_edTxt4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(e_edTxt4.text.length == 1){
                    e_edTxt5.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        e_edTxt5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(e_edTxt5.text.length == 1){
                    e_edTxt6.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        header.text = "Please enter the 6-digit OTP sent to your email"
        btnContinue.setOnClickListener {
            val otp_1 = e_edTxt1.text.toString()
            val otp_2 = e_edTxt2.text.toString()
            val otp_3 = e_edTxt3.text.toString()
            val otp_4 = e_edTxt4.text.toString()
            val otp_5 = e_edTxt5.text.toString()
            val otp_6 = e_edTxt6.text.toString()
            if(otp_1.isNotEmpty() && otp_2.isNotEmpty() && otp_3.isNotEmpty() && otp_4.isNotEmpty()
                && otp_5.isNotEmpty() && otp_6.isNotEmpty()){
                val otp = otp_1+otp_2+otp_3+otp_4+otp_5+otp_6

                if(requireActivity().isConnectedToInternet()){
                    verifyEmail(binding.emailTxt.text.toString(),otp,accessToken)
                    //dialog.dismiss()
                }else{
                    Toast.makeText(requireActivity(),"No internet connection.",Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(requireActivity(),"Invalid OTP",Toast.LENGTH_SHORT).show()
            }
        }

        emailOtpDialog.setCancelable(true)
        emailOtpDialog.setContentView(view)
        emailOtpDialog.show()

    }

    private fun showMobileNumberVerifyBottomSheet() {
        numberOtpDialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.otp_bottomsheet_lay, null)
        val btnContinue = view.findViewById<TextView>(R.id.verify_otp_btn)
        val header = view.findViewById<TextView>(R.id.header_txt_1)
        val resend = view.findViewById<TextView>(R.id.resend_tv)
        val timerTv = view.findViewById<TextView>(R.id.timer_tv)

        resend.gone()
        startTimer(timerTv,resend)
        m_edTxt1 = view.findViewById<EditText>(R.id.ed_txt1)
        m_edTxt2 = view.findViewById<EditText>(R.id.ed_txt2)
        m_edTxt3 = view.findViewById<EditText>(R.id.ed_txt3)
        m_edTxt4 = view.findViewById<EditText>(R.id.ed_txt4)
        m_edTxt5 = view.findViewById<EditText>(R.id.ed_txt5)
        m_edTxt6 = view.findViewById<EditText>(R.id.ed_txt6)

        resend.setOnClickListener {
            startTimer(timerTv,resend)
            resend.gone()
            sendNumberOtp(accessToken, binding.mobileTxt.text.toString(),true)
        }

        m_edTxt1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(m_edTxt1.text.length == 1){
                    m_edTxt2.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        m_edTxt2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(m_edTxt2.text.length == 1){
                    m_edTxt3.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        m_edTxt3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(m_edTxt3.text.length == 1){
                    m_edTxt4.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        m_edTxt4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(m_edTxt4.text.length == 1){
                    m_edTxt5.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        m_edTxt5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(m_edTxt5.text.length == 1){
                    m_edTxt6.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        header.text = "Please enter the 6-digit OTP sent to your phone number"
        btnContinue.setOnClickListener {
            val otp_1 = m_edTxt1.text.toString()
            val otp_2 = m_edTxt2.text.toString()
            val otp_3 = m_edTxt3.text.toString()
            val otp_4 = m_edTxt4.text.toString()
            val otp_5 = m_edTxt5.text.toString()
            val otp_6 = m_edTxt6.text.toString()
            if(otp_1.isNotEmpty() && otp_2.isNotEmpty() && otp_3.isNotEmpty() && otp_4.isNotEmpty()
                && otp_5.isNotEmpty() && otp_6.isNotEmpty()){
                val otp = otp_1+otp_2+otp_3+otp_4+otp_5+otp_6
                if(requireActivity().isConnectedToInternet()){
                    verifyMobileOtp(binding.mobileTxt.text.toString(),otp,accessToken)
                    //dialog.dismiss()
                }else{
                    Toast.makeText(requireActivity(),"No internet connection.",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireActivity(),"Invalid OTP",Toast.LENGTH_SHORT).show()
            }
        }
        numberOtpDialog.setCancelable(true)
        numberOtpDialog.setContentView(view)
        numberOtpDialog.show()
    }

    fun startTimer(textView: TextView, resendView: TextView) {
        cTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                textView.setText("OTP will be expired in: " + millisUntilFinished / 1000 + " seconds");
            }
            override fun onFinish() {
                cancelTimer()
                resendView.visible()
            }
        }
        (cTimer as CountDownTimer).start()
    }

    fun cancelTimer() {
        if (cTimer != null) cTimer!!.cancel()
    }

    private fun signUp(
        name: String,
        email: String,
        phone: String,
        password: String,
        is_above_eighteen: Int,
        assign_class_id: Int,
        board_id: Int,
        parent_name: String?
    ){
        val loader = requireActivity().loadingDialog(false,R.raw.loader_bar)
        loader.show()
        mSignUpViewModel.signUp(name, email, phone, password, is_above_eighteen, assign_class_id, board_id, parent_name).observe(viewLifecycleOwner) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {

                        if (outcome.data.access_token != null) {
                            PrefManager.setKeyAuthToken(outcome.data.access_token)
                        }
                        PrefManager.setLogInStatus(true)

                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        requireActivity().finish()
                        Toast.makeText(requireActivity(), outcome.data.message, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireActivity(), outcome.data.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun sendNumberOtp(
        token: String,
        phone: String,
        isResend: Boolean
    ){
        binding.progressBar.visible()
        mSendNumberViewModel.sendNumber(token,phone).observe(viewLifecycleOwner) { outcome ->
            binding.progressBar.gone()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        if(isResend){
                            Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        }else{
                            showMobileNumberVerifyBottomSheet()
                        }
                    } else {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun verifyMobileOtp(
        phone: String,
        otp: String,
        token: String,
    ){
        binding.progressBar.visible()
        mVerifyMobileOtpViewModel.verifyMobileOtp(phone, otp, token).observe(viewLifecycleOwner) { outcome ->
            binding.progressBar.gone()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        binding.verifyMobileHtv.gone()
                        mobileVerify = phone
                        binding.mobileTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_check_24), null)
                        numberOtpDialog.dismiss()
                        cancelTimer()
                    } else {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        m_edTxt1.text = null
                        m_edTxt2.text = null
                        m_edTxt3.text = null
                        m_edTxt4.text = null
                        m_edTxt5.text = null
                        m_edTxt6.text = null
                        m_edTxt1.requestFocus()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun sendEmailOtp(
        email: String,
        token: String,
        isResend: Boolean
    ){
        binding.progressBar.visible()
        mSendEmailOtpViewModel.sendEmailOtp(email, token).observe(viewLifecycleOwner) { outcome ->
            binding.progressBar.gone()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        if(isResend){
                            Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        }else{
                            emailVerifyBottomSheet()
                            requireActivity().hideSoftKeyboard()
                        }

                    } else {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun verifyEmail(
        email: String,
        otp: String,
        token: String,
    ){
        binding.progressBar.visible()
        mVerifyEmailViewModel.verifyEmail(email, otp, token).observe(viewLifecycleOwner) { outcome ->
            binding.progressBar.gone()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        binding.verifyEmailHtv.gone()
                        emailVerify = binding.emailTxt.text.toString()
                        binding.emailTxt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_check_24), null)
                        emailOtpDialog.dismiss()
                        cancelTimer()
                    } else {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        e_edTxt1.text = null
                        e_edTxt2.text = null
                        e_edTxt3.text = null
                        e_edTxt4.text = null
                        e_edTxt5.text = null
                        e_edTxt6.text = null
                        e_edTxt1.requestFocus()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun showAgePopUp(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Alert")
        builder.setMessage("Are you above 18 years old ?")
        builder.setIcon(R.drawable.ic_baseline_warning_24)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            binding.parentsNameTxtLay.gone()
            above18 = true
            is_above18 = 1
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
            binding.parentsNameTxtLay.visible()
            above18 = false
            is_above18 = 0
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    //board class
    private fun getAllClassList(
        board_id: Int
    ){
        binding.classSpinnerLay.visible()
        binding.classSpinner.gone()
        binding.noClassTv.gone()
        binding.classShimmerView.visible()
        binding.classShimmerView.startShimmer()
        mGetAllClassViewModel.getAllClass(board_id).observe(requireActivity()) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.classShimmerView.stopShimmer()
                        binding.classShimmerView.gone()
                        if(outcome.data.result.result.all_class.isNotEmpty()){
                            for (i in outcome.data.result.result.all_class){
                                classList.add(i.name)
                                classIdList.add(i.id)
                            }
                            binding.noClassTv.gone()
                            binding.classSpinner.visible()
                            binding.requireBelowClass.visible()
                            setupClassSpinner()
                        }else{
                            binding.classSpinner.gone()
                            binding.requireBelowClass.gone()
                            binding.noClassTv.visible()
                            std = 0
                        }
                    } else {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun getBoards(){
        boardList = mutableListOf()
        binding.boardSpinner.gone()
        binding.noBoardTv.gone()
        binding.boardShimmerView.visible()
        binding.boardShimmerView.startShimmer()
        mGetBoardsViewModel.getBoards().observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.boardShimmerView.gone()
                        binding.boardShimmerView.stopShimmer()

                        if(outcome.data.result.board != null && outcome.data.result.board.size > 0){

                            boardList.add("Select Board")
                            boardIdList.add(0)

                            binding.boardSpinner.visible()
                            binding.noBoardTv.gone()

                            for (i in outcome.data.result.board){
                                boardList.add(i.exam_board)
                                boardIdList.add(i.id)
                            }
                            setupBoardSpinner(boardList)
                        }else{
                            binding.boardSpinner.gone()
                            binding.noBoardTv.visible()
                        }


                    } else {
                        Toast.makeText(requireActivity(), outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun setupBoardSpinner(boardList: List<String>) {
        val arrayAdapter2 = ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_dropdown_item,boardList)
        binding.boardSpinner.adapter = arrayAdapter2
        binding.boardSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                board = boardList[p2]
                boardId = boardIdList[p2]
                if(p2 != 0){
                    classList = mutableListOf()
                    getAllClassList(boardId)
                    binding.requireBelowBoard.gone()
                }else{
                    board = ""
                    boardId = 0
                    classList = mutableListOf()
                    binding.classSpinnerLay.gone()
                    binding.requireBelowClass.gone()
                    binding.requireBelowBoard.visible()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }
        }
    }

    private fun setupClassSpinner() {
        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            classList
        )
        binding.classSpinner.adapter = arrayAdapter
        binding.classSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                class_global = classList[p2]
                std = classIdList[p2]
                if(p2==0){
                    binding.requireBelowClass.visible()
                }else{
                    binding.requireBelowClass.gone()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTimer()
    }
}