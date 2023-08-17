package com.example.abithshiksha.model.network

import com.example.abithshiksha.model.pojo.add_ons.GetAddonsResponse
import com.example.abithshiksha.model.pojo.add_review.AddReviewRequest
import com.example.abithshiksha.model.pojo.add_review.AddReviewResponse
import com.example.abithshiksha.model.pojo.remove_cart.RemoveCartResponse
import com.example.abithshiksha.model.pojo.add_to_cart.AddToCartRequest
import com.example.abithshiksha.model.pojo.add_to_cart.AddToCartResponse
import com.example.abithshiksha.model.pojo.banner.GetBannerResponse
import com.example.abithshiksha.model.pojo.change_password.ChangePasswordRequest
import com.example.abithshiksha.model.pojo.change_password.ChangePasswordResponse
import com.example.abithshiksha.model.pojo.edit_profile.EditProfileRequest
import com.example.abithshiksha.model.pojo.edit_profile.EditProfileResponse
import com.example.abithshiksha.model.pojo.fp_change_pass.FpChangePassRequest
import com.example.abithshiksha.model.pojo.fp_change_pass.FpChangePassResponse
import com.example.abithshiksha.model.pojo.fp_send_otp.SendOtpRequest
import com.example.abithshiksha.model.pojo.fp_send_otp.SendOtpResponse
import com.example.abithshiksha.model.pojo.fp_verify_otp.VerifyOtpRequest
import com.example.abithshiksha.model.pojo.fp_verify_otp.VerifyOtpResponse
import com.example.abithshiksha.model.pojo.get_addons.GetSelectedAddonsResponse
import com.example.abithshiksha.model.pojo.get_all_class.GetAllClassResponse
import com.example.abithshiksha.model.pojo.get_all_performance.GetAllPerformanceResponse
import com.example.abithshiksha.model.pojo.get_article.GetArticleResponse
import com.example.abithshiksha.model.pojo.get_boards.GetBoardsResponse
import com.example.abithshiksha.model.pojo.get_cart_details.GetCartDetailsResponse
import com.example.abithshiksha.model.pojo.get_carts.GetCartsResponse
import com.example.abithshiksha.model.pojo.get_class_list.GetClassListResponse
import com.example.abithshiksha.model.pojo.get_course_subject.GetCourseSubjectResponse
import com.example.abithshiksha.model.pojo.get_filtered_subject.GetFilteredSubjectResponse
import com.example.abithshiksha.model.pojo.get_filtered_subject.GetFilteredSubjectsRequest
import com.example.abithshiksha.model.pojo.get_gallery.GetGalleryResponse
import com.example.abithshiksha.model.pojo.get_lessons.GetLessonsResponse
import com.example.abithshiksha.model.pojo.get_mcq.GetMcqResponse
import com.example.abithshiksha.model.pojo.get_mcq_sets.GetMcqSetsResponse
import com.example.abithshiksha.model.pojo.get_order_id.GetOrderIdResponse
import com.example.abithshiksha.model.pojo.get_orders.GetOrdersResponse
import com.example.abithshiksha.model.pojo.get_pdf.GetPdfResponse
import com.example.abithshiksha.model.pojo.get_profile.GetProfileResponse
import com.example.abithshiksha.model.pojo.get_purchase_receipt.GetPurchaseReceiptResponse
import com.example.abithshiksha.model.pojo.get_review.GetReviewResponse
import com.example.abithshiksha.model.pojo.get_subject_performance.GetSubjectPerformanceResponse
import com.example.abithshiksha.model.pojo.get_time_table.GetTimeTableResponse
import com.example.abithshiksha.model.pojo.get_topic_list.GetTopicListResponse
import com.example.abithshiksha.model.pojo.get_topics.GetTopicResponse
import com.example.abithshiksha.model.pojo.get_videos.GetVideosResponse
import com.example.abithshiksha.model.pojo.log_out.LogoutResponse
import com.example.abithshiksha.model.pojo.login_model.LoginRequest
import com.example.abithshiksha.model.pojo.login_model.LoginResponseModel
import com.example.abithshiksha.model.pojo.mcq_result.GetMcqResultResponse
import com.example.abithshiksha.model.pojo.payment_status.PaymentStatusRequest
import com.example.abithshiksha.model.pojo.payment_status.PaymentStatusResponse
import com.example.abithshiksha.model.pojo.puchase_history.GetPurchaseHistoryResponse
import com.example.abithshiksha.model.pojo.send_email_otp.SendEmailOtpRequest
import com.example.abithshiksha.model.pojo.send_email_otp.SendEmailOtpResponse
import com.example.abithshiksha.model.pojo.send_number_otp.SendNumberOtpRequest
import com.example.abithshiksha.model.pojo.send_number_otp.SendNumberOtpResponse
import com.example.abithshiksha.model.pojo.sign_up.SignUpRequest
import com.example.abithshiksha.model.pojo.sign_up.SignUpResponse
import com.example.abithshiksha.model.pojo.subject_details.GetSubjectDetailsResponse
import com.example.abithshiksha.model.pojo.submit_mcq.request.SubmitMcqRequest
import com.example.abithshiksha.model.pojo.submit_mcq.response.SubmitMcqResponse
import com.example.abithshiksha.model.pojo.suggestion_courses.GetSuggestionCoursesResponse
import com.example.abithshiksha.model.pojo.upcomming_response.GetUpcommingCoursesResponse
import com.example.abithshiksha.model.pojo.upload_profile_pic.UploadProfilePicResponse
import com.example.abithshiksha.model.pojo.verify_email.VerifyEmailRequest
import com.example.abithshiksha.model.pojo.verify_email.VerifyEmailResponse
import com.example.abithshiksha.model.pojo.verify_mobile_otp.VerifyMobileOtpRequest
import com.example.abithshiksha.model.pojo.verify_mobile_otp.VerifyMobileOtpResponse
import com.example.abithshiksha.model.pojo.watch_time.SubmitWatchTimeRequest
import com.example.abithshiksha.model.pojo.watch_time.SubmitWatchTimeResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiInterface {
    @POST("login")
    suspend fun login(@Body body: LoginRequest?): LoginResponseModel

    @POST("signup")
    suspend fun signup(@Body body: SignUpRequest?): SignUpResponse

    @GET("banner")
    suspend fun getBanner(
        @Header("Authorization") token: String,
    ): GetBannerResponse

    @POST("subjects")
    suspend fun getFilteredSubjects(
        @Body body: GetFilteredSubjectsRequest?,
        @Header("Authorization") token: String,
    ): GetFilteredSubjectResponse

    @GET("homepage/upcomming")
    suspend fun getUpcomingCourse(
        @Header("Authorization") token: String,
    ): GetUpcommingCoursesResponse

    @GET("homepage/courses")
    suspend fun getSuggestionCourse(
        @Header("Authorization") token: String,
    ): GetSuggestionCoursesResponse

    @GET("cart/remove")
    suspend fun removeCart(
        @Header("Authorization") token: String,
        @Query("cart_id") cart_id: Int?,
    ): RemoveCartResponse?

    @GET("gallery")
    suspend fun getGallery(
        @Header("Authorization") token: String,
    ): GetGalleryResponse?

    @GET("user")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): GetProfileResponse?

    @GET("subject-details")
    suspend fun getSubjectDetails(
        @Header("Authorization") token: String,
        @Query("subject_id") subject_id: Int?,
    ): GetSubjectDetailsResponse?

    @POST("send-mobile-otp")
    suspend fun sendNumberOtp(
        @Body body: SendNumberOtpRequest?,
        @Header("Authorization") token: String,
    ): SendNumberOtpResponse

    @POST("verify-mobile-otp")
    suspend fun verifyMobileOtp(
        @Body body: VerifyMobileOtpRequest?,
        @Header("Authorization") token: String,
    ): VerifyMobileOtpResponse

    @POST("send-email-otp")
    suspend fun sendEmailOtp(
        @Body body: SendEmailOtpRequest?,
        @Header("Authorization") token: String,
    ): SendEmailOtpResponse

    @POST("verify-email-otp")
    suspend fun verifyEmail(
        @Body body: VerifyEmailRequest?,
        @Header("Authorization") token: String,
    ): VerifyEmailResponse

    @GET("subject/lessons")
    suspend fun getLessons(
        @Header("Authorization") token: String,
        @Query("subject_id") subject_id: Int?,
        @Query("page") page: Int?,
    ): GetLessonsResponse?

    @POST("cart/store")
    suspend fun addToCart(
        @Body body: AddToCartRequest?,
        @Header("Authorization") token: String,
    ): AddToCartResponse

    @GET("cart/cart-details")
    suspend fun getCartDetails(
        @Header("Authorization") token: String,
        @Query("cart_id") cart_id: Int?,
    ): GetCartDetailsResponse?

    @GET("cart")
    suspend fun getCart(
        @Header("Authorization") token: String,
    ): GetCartsResponse?

    @GET("subject/lesson/topic")
    suspend fun getTopic(
        @Header("Authorization") token: String,
        @Query("lesson_id") lesson_id: Int?,
        @Query("page") page: Int?,
    ): GetTopicResponse?

    @GET("subject/lesson/video")
    suspend fun getVideos(
        @Header("Authorization") token: String,
        @Query("lesson_id") lesson_id: Int?,
        @Query("page") page: Int?,
    ): GetVideosResponse?

    @GET("subject/lesson/pdf")
    suspend fun getPdf(
        @Header("Authorization") token: String,
        @Query("lesson_id") lesson_id: Int?,
        @Query("page") page: Int?,
    ): GetPdfResponse?

    @GET("all-class")
    suspend fun getClass(
        @Header("Authorization") token: String,
        @Query("board_name") board_name: String?,
    ): GetClassListResponse?

    @GET("subject/lesson/content")
    suspend fun getArticle(
        @Header("Authorization") token: String,
        @Query("lesson_id") lesson_id: Int?,
        @Query("page") page: Int?,
    ): GetArticleResponse?

    @GET("subject/mcq")
    suspend fun getMcqSets(
        @Header("Authorization") token: String,
        @Query("lesson_id") lesson_id: Int?,
    ): GetMcqSetsResponse?

    @GET("subject/mcq-question")
    suspend fun getMcq(
        @Header("Authorization") token: String,
        @Query("set_id") set_id: Int?,
        @Query("page") page: Int?,
    ): GetMcqResponse?

    @POST("subject/mcq/submit")
    suspend fun submitMcq(
        @Body body: SubmitMcqRequest?,
        @Header("Authorization") token: String,
    ): SubmitMcqResponse

    @GET("subject/mcq/result")
    suspend fun getMcqResult(
        @Header("Authorization") token: String,
        @Query("id") id: Int?,
    ): GetMcqResultResponse?

    @GET("logout")
    suspend fun logout(
        @Header("Authorization") token: String,
    ): LogoutResponse?

    @POST("user/update")
    suspend fun editProfile(
        @Body body: EditProfileRequest?,
        @Header("Authorization") token: String,
    ): EditProfileResponse

    @Multipart
    @POST("user/profile/update")
    suspend fun uploadProfilePic(
        @Part image: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): UploadProfilePicResponse

    @GET("user/courses")
    suspend fun getOrders(
        @Header("Authorization") token: String,
    ): GetOrdersResponse?

    @POST("user/password-reset")
    suspend fun changePassword(
        @Body body: ChangePasswordRequest?,
        @Header("Authorization") token: String,
    ): ChangePasswordResponse?

    @GET("user/courses/subject")
    suspend fun getCourseSubject(
        @Header("Authorization") token: String,
        @Query("cart_id") cart_id: Int?,
    ): GetCourseSubjectResponse?

    @POST("review/store")
    suspend fun addReview(
        @Body body: AddReviewRequest?,
        @Header("Authorization") token: String,
    ): AddReviewResponse?

    @GET("review")
    suspend fun getReview(
        @Header("Authorization") token: String,
        @Query("subject_id") subject_id: Int?,
    ): GetReviewResponse?

    @GET("payment/order-generate")
    suspend fun getOrderId(
        @Header("Authorization") token: String,
        @Query("cart_id") cart_id: Int?,
    ): GetOrderIdResponse?

    @POST("payment")
    suspend fun paymentStatus(
        @Body body: PaymentStatusRequest?,
        @Header("Authorization") token: String,
    ): PaymentStatusResponse?

    @POST("subject/lesson/video/watch-time")
    suspend fun submitWatchTime(
        @Body body: SubmitWatchTimeRequest?,
        @Header("Authorization") token: String,
    ): SubmitWatchTimeResponse?

    @POST("user/sendotp")
    suspend fun fpSendOtp(
        @Body body: SendOtpRequest?,
    ): SendOtpResponse?

    @POST("user/verifyotp")
    suspend fun fpVerifyOtp(
        @Body body: VerifyOtpRequest?,
    ): VerifyOtpResponse?

    @POST("user/verifyotp")
    suspend fun fpChangePass(
        @Body body: FpChangePassRequest?,
    ): FpChangePassResponse?

    @GET("user/performance")
    suspend fun getAllPerformance(
        @Header("Authorization") token: String,
    ): GetAllPerformanceResponse?

    @GET("user/performance")
    suspend fun getSubjectPerformance(
        @Header("Authorization") token: String,
        @Query("id") id: Int?,
    ): GetSubjectPerformanceResponse?

    @GET("board")
    suspend fun getBoards(): GetBoardsResponse?

    @GET("subject/lesson/topic-details")
    suspend fun getTopicList(
        @Header("Authorization") token: String,
        @Query("lesson_id") lesson_id: Int?,
    ): GetTopicListResponse?

    @GET("get-all-class")
    suspend fun getAllClass(
        @Query("board_id") board_id: Int?,
    ): GetAllClassResponse?

    @GET("user/purchase-history")
    suspend fun getPurchaseHistory(
        @Header("Authorization") token: String
    ): GetPurchaseHistoryResponse?

    @GET("user/purchase-history/receipt")
    suspend fun getPurchaseReceipt(
        @Header("Authorization") token: String,
        @Query("order_id") order_id: Int?,
    ): GetPurchaseReceiptResponse?

    @GET("user/time-table")
    suspend fun getTimeTable(
        @Header("Authorization") token: String,
    ): GetTimeTableResponse?

    @GET("addon/get")
    suspend fun getAddOns(
        @Header("Authorization") token: String,
        @Query("board_id") borad_id: Int?,
        @Query("class") standard: String?
    ): GetAddonsResponse?

    @GET("addon/get-selected")
    suspend fun getSelectedAddOns(
        @Header("Authorization") token: String,
    ): GetSelectedAddonsResponse?
}