
import com.example.abithshiksha.model.repo.add_review_repo.AddReviewRepository
import com.example.abithshiksha.model.repo.add_review_repo.AddReviewRepositoryImpl
import com.example.abithshiksha.model.repo.add_to_cart_repo.AddToCartRepository
import com.example.abithshiksha.model.repo.add_to_cart_repo.AddToCartRepositoryImpl
import com.example.abithshiksha.model.repo.change_password_repo.ChangePasswordRepository
import com.example.abithshiksha.model.repo.change_password_repo.ChangePasswordRepositoryImpl
import com.example.abithshiksha.model.repo.download_receipt_repo.DownloadPurchaseReceiptRepository
import com.example.abithshiksha.model.repo.download_receipt_repo.DownloadPurchaseReceiptRepositoryImpl
import com.example.abithshiksha.model.repo.edit_profile_repo.EditProfileRepository
import com.example.abithshiksha.model.repo.edit_profile_repo.EditProfileRepositoryImpl
import com.example.abithshiksha.model.repo.fp_change_pass_repo.FpChangePassRepository
import com.example.abithshiksha.model.repo.fp_change_pass_repo.FpChangePassRepositoryImpl
import com.example.abithshiksha.model.repo.fp_send_otp_repo.SendOtpRepository
import com.example.abithshiksha.model.repo.fp_send_otp_repo.SendOtpRepositoryImpl
import com.example.abithshiksha.model.repo.fp_verify_otp_repo.VerifyOtpRepository
import com.example.abithshiksha.model.repo.fp_verify_otp_repo.VerifyOtpRepositoryImpl
import com.example.abithshiksha.model.repo.get_all_class_repo.GetAllClassRepository
import com.example.abithshiksha.model.repo.get_all_class_repo.GetAllClassRepositoryImpl
import com.example.abithshiksha.model.repo.get_all_performance_repo.GetAllPerformanceRepository
import com.example.abithshiksha.model.repo.get_all_performance_repo.GetAllPerformanceRepositoryImpl
import com.example.abithshiksha.model.repo.get_article_repo.GetArticleRepository
import com.example.abithshiksha.model.repo.get_article_repo.GetArticleRepositoryImpl
import com.example.abithshiksha.model.repo.get_banner_repo.GetBannerRepository
import com.example.abithshiksha.model.repo.get_banner_repo.GetBannerRepositoryImpl
import com.example.abithshiksha.model.repo.get_boards_repo.GetBoardsRepository
import com.example.abithshiksha.model.repo.get_boards_repo.GetBoardsRepositoryImpl
import com.example.abithshiksha.model.repo.get_cart_details_repo.GetCartDetailsRepository
import com.example.abithshiksha.model.repo.get_cart_details_repo.GetCartDetailsRepositoryImpl
import com.example.abithshiksha.model.repo.get_carts_repo.GetCartsRepository
import com.example.abithshiksha.model.repo.get_carts_repo.GetCartsRepositoryImpl
import com.example.abithshiksha.model.repo.get_class_list_repo.GetClassListRepository
import com.example.abithshiksha.model.repo.get_class_list_repo.GetClassListRepositoryImpl
import com.example.abithshiksha.model.repo.get_course_subject_repo.GetCourseSubjectRepository
import com.example.abithshiksha.model.repo.get_course_subject_repo.GetCourseSubjectRepositoryImpl
import com.example.abithshiksha.model.repo.get_filtered_subject_repo.GetFilteredSubjectRepository
import com.example.abithshiksha.model.repo.get_filtered_subject_repo.GetFilteredSubjectRepositoryImpl
import com.example.abithshiksha.model.repo.get_gallery_repo.GetGalleryRepository
import com.example.abithshiksha.model.repo.get_gallery_repo.GetGalleryRepositoryImpl
import com.example.abithshiksha.model.repo.get_lessons_repo.GetLessonsRepository
import com.example.abithshiksha.model.repo.get_lessons_repo.GetLessonsRepositoryImpl
import com.example.abithshiksha.model.repo.get_mcq_repo.GetMcqRepository
import com.example.abithshiksha.model.repo.get_mcq_repo.GetMcqRepositoryImpl
import com.example.abithshiksha.model.repo.get_mcq_result_repo.GetMcqResultRepository
import com.example.abithshiksha.model.repo.get_mcq_result_repo.GetMcqResultRepositoryImpl
import com.example.abithshiksha.model.repo.get_mcq_set_repo.GetMcqSetsRepository
import com.example.abithshiksha.model.repo.get_mcq_set_repo.GetMcqSetsRepositoryImpl
import com.example.abithshiksha.model.repo.get_order_id_repo.GetOrderIdRepository
import com.example.abithshiksha.model.repo.get_order_id_repo.GetOrderIdRepositoryImpl
import com.example.abithshiksha.model.repo.get_orders_repo.GetOrdersRepository
import com.example.abithshiksha.model.repo.get_orders_repo.GetOrdersRepositoryImpl
import com.example.abithshiksha.model.repo.get_pdf_repo.GetPdfRepository
import com.example.abithshiksha.model.repo.get_pdf_repo.GetPdfRepositoryImpl
import com.example.abithshiksha.model.repo.get_profile_repo.GetProfileRepository
import com.example.abithshiksha.model.repo.get_profile_repo.GetProfileRepositoryImpl
import com.example.abithshiksha.model.repo.get_review_repo.GetReviewRepository
import com.example.abithshiksha.model.repo.get_review_repo.GetReviewRepositoryImpl
import com.example.abithshiksha.model.repo.get_subject_performance.GetSubjectPerformanceRepository
import com.example.abithshiksha.model.repo.get_subject_performance.GetSubjectPerformanceRepositoryImpl
import com.example.abithshiksha.model.repo.get_suggestion_courses.GetSuggestionCoursesRepository
import com.example.abithshiksha.model.repo.get_suggestion_courses.GetSuggestionCoursesRepositoryImpl
import com.example.abithshiksha.model.repo.get_time_table_repo.GetTimeTableRepository
import com.example.abithshiksha.model.repo.get_time_table_repo.GetTimeTableRepositoryImpl
import com.example.abithshiksha.model.repo.get_topic_list_repo.GetTopicListRepository
import com.example.abithshiksha.model.repo.get_topic_list_repo.GetTopicListRepositoryImpl
import com.example.abithshiksha.model.repo.get_topic_repo.GetTopicRepository
import com.example.abithshiksha.model.repo.get_topic_repo.GetTopicRepositoryImpl
import com.example.abithshiksha.model.repo.get_videos_repo.GetVideosRepository
import com.example.abithshiksha.model.repo.get_videos_repo.GetVideosRepositoryImpl
import com.example.abithshiksha.model.repo.login_repo.LoginRepository
import com.example.abithshiksha.model.repo.login_repo.LoginRepositoryImpl
import com.example.abithshiksha.model.repo.logout_repo.LogoutRepository
import com.example.abithshiksha.model.repo.logout_repo.LogoutRepositoryImpl
import com.example.abithshiksha.model.repo.payment_status_repo.PaymentStatusRepository
import com.example.abithshiksha.model.repo.payment_status_repo.PaymentStatusRepositoryImpl
import com.example.abithshiksha.model.repo.purchase_history.GetPurchaseHistoryRepository
import com.example.abithshiksha.model.repo.purchase_history.GetPurchaseHistoryRepositoryImpl
import com.example.abithshiksha.model.repo.remove_cart_repo.RemoveCartRepository
import com.example.abithshiksha.model.repo.remove_cart_repo.RemoveCartRepositoryImpl
import com.example.abithshiksha.model.repo.send_email_otp_repo.SendEmailOtpRepository
import com.example.abithshiksha.model.repo.send_email_otp_repo.SendEmailOtpRepositoryImpl
import com.example.abithshiksha.model.repo.send_number_otp_repo.SendNumberOtpRepository
import com.example.abithshiksha.model.repo.send_number_otp_repo.SendNumberOtpRepositoryImpl
import com.example.abithshiksha.model.repo.sign_up_repo.SignUpRepository
import com.example.abithshiksha.model.repo.sign_up_repo.SignUpRepositoryImpl
import com.example.abithshiksha.model.repo.subject_details_repo.SubjectDetailsRepository
import com.example.abithshiksha.model.repo.subject_details_repo.SubjectDetailsRepositoryImpl
import com.example.abithshiksha.model.repo.submit_mcq_repo.SubmitMcqRepository
import com.example.abithshiksha.model.repo.submit_mcq_repo.SubmitMcqRepositoryImpl
import com.example.abithshiksha.model.repo.submit_watch_time_repo.SubmitWatchTimeRepository
import com.example.abithshiksha.model.repo.submit_watch_time_repo.SubmitWatchTimeRepositoryImpl
import com.example.abithshiksha.model.repo.upcomming_courses_repo.UpcommingCoursesRepository
import com.example.abithshiksha.model.repo.upcomming_courses_repo.UpcommingCoursesRepositoryImpl
import com.example.abithshiksha.model.repo.upload_profile_pic_repo.UploadProfilePicRepository
import com.example.abithshiksha.model.repo.upload_profile_pic_repo.UploadProfilePicRepositoryImpl
import com.example.abithshiksha.model.repo.verify_email_repo.VerifyEmailRepository
import com.example.abithshiksha.model.repo.verify_email_repo.VerifyEmailRepositoryImpl
import com.example.abithshiksha.model.repo.verify_mobile_otp_repo.VerifyMobileOtpRepository
import com.example.abithshiksha.model.repo.verify_mobile_otp_repo.VerifyMobileOtpRepositoryImpl
import com.example.abithshiksha.view_model.*
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    viewModel { LoginViewModel(get()) }

    single<SignUpRepository> { SignUpRepositoryImpl(get()) }
    viewModel { SignUpViewModel(get()) }

    single<GetBannerRepository> { GetBannerRepositoryImpl(get()) }
    viewModel { GetBannerViewModel(get()) }

    single<GetFilteredSubjectRepository> { GetFilteredSubjectRepositoryImpl(get()) }
    viewModel { GetFilteredSubjectViewModel(get()) }

    single<UpcommingCoursesRepository> { UpcommingCoursesRepositoryImpl(get()) }
    viewModel { UpcommingCoursesViewModel(get()) }

    single<GetSuggestionCoursesRepository> { GetSuggestionCoursesRepositoryImpl(get()) }
    viewModel { GetSuggestionCoursesViewModel(get()) }

    single<RemoveCartRepository> { RemoveCartRepositoryImpl(get()) }
    viewModel { RemoveCartViewModel(get()) }

    single<GetGalleryRepository> { GetGalleryRepositoryImpl(get()) }
    viewModel { GetGalleryViewModel(get()) }

    single<GetProfileRepository> { GetProfileRepositoryImpl(get()) }
    viewModel { GetProfileViewModel(get()) }

    single<SubjectDetailsRepository> { SubjectDetailsRepositoryImpl(get()) }
    viewModel { GetSubjectDetailsViewModel(get()) }

    single<SendNumberOtpRepository> { SendNumberOtpRepositoryImpl(get()) }
    viewModel { SendNumberViewModel(get()) }

    single<VerifyMobileOtpRepository> { VerifyMobileOtpRepositoryImpl(get()) }
    viewModel { VerifyMobileOtpViewModel(get()) }

    single<SendEmailOtpRepository> { SendEmailOtpRepositoryImpl(get()) }
    viewModel { SendEmailOtpViewModel(get()) }

    single<VerifyEmailRepository> { VerifyEmailRepositoryImpl(get()) }
    viewModel { VerifyEmailViewModel(get()) }

    single<GetLessonsRepository> { GetLessonsRepositoryImpl(get()) }
    viewModel { GetLessonsViewModel(get()) }

    single<AddToCartRepository> { AddToCartRepositoryImpl(get()) }
    viewModel { AddToCartViewModel(get()) }

    single<GetCartDetailsRepository> { GetCartDetailsRepositoryImpl(get()) }
    viewModel { GetCartDetailsViewModel(get()) }

    single<GetCartsRepository> { GetCartsRepositoryImpl(get()) }
    viewModel { GetCartsViewModel(get()) }

    single<GetTopicRepository> { GetTopicRepositoryImpl(get()) }
    viewModel { GetTopicViewModel(get()) }

    single<GetVideosRepository> { GetVideosRepositoryImpl(get()) }
    viewModel { GetVideosViewModel(get()) }

    single<GetPdfRepository> { GetPdfRepositoryImpl(get()) }
    viewModel { GetPdfViewModel(get()) }

    single<GetClassListRepository> { GetClassListRepositoryImpl(get()) }
    viewModel { GetClassListViewModel(get()) }

    single<GetArticleRepository> { GetArticleRepositoryImpl(get()) }
    viewModel { GetArticleViewModel(get()) }

    single<GetMcqSetsRepository> { GetMcqSetsRepositoryImpl(get()) }
    viewModel { GetMcqSetsViewModel(get()) }

    single<GetMcqRepository> { GetMcqRepositoryImpl(get()) }
    viewModel { GetMcqViewModel(get()) }

    single<SubmitMcqRepository> { SubmitMcqRepositoryImpl(get()) }
    viewModel { SubmitMcqViewModel(get()) }

    single<GetMcqResultRepository> { GetMcqResultRepositoryImpl(get()) }
    viewModel { GetMcqResultViewModel(get()) }

    single<LogoutRepository> { LogoutRepositoryImpl(get()) }
    viewModel { LogoutViewModel(get()) }

    single<EditProfileRepository> { EditProfileRepositoryImpl(get()) }
    viewModel { EditProfileViewModel(get()) }

    single<UploadProfilePicRepository> { UploadProfilePicRepositoryImpl(get()) }
    viewModel { UploadProfilePicViewModel(get()) }

    single<GetOrdersRepository> { GetOrdersRepositoryImpl(get()) }
    viewModel { GetOrdersViewModel(get()) }

    single<ChangePasswordRepository> { ChangePasswordRepositoryImpl(get()) }
    viewModel { ChangePasswordViewModel(get()) }

    single<GetCourseSubjectRepository> { GetCourseSubjectRepositoryImpl(get()) }
    viewModel { GetCourseSubjectViewModel(get()) }

    single<AddReviewRepository> { AddReviewRepositoryImpl(get()) }
    viewModel { AddReviewViewModel(get()) }

    single<GetReviewRepository> { GetReviewRepositoryImpl(get()) }
    viewModel { GetReviewViewModel(get()) }

    single<GetOrderIdRepository> { GetOrderIdRepositoryImpl(get()) }
    viewModel { GetOrderIdViewModel(get()) }

    single<PaymentStatusRepository> { PaymentStatusRepositoryImpl(get()) }
    viewModel { PaymentStatusViewModel(get()) }

    single<SubmitWatchTimeRepository> { SubmitWatchTimeRepositoryImpl(get()) }
    viewModel { SubmitWatchTimeViewModel(get()) }

    single<SendOtpRepository> { SendOtpRepositoryImpl(get()) }
    viewModel { SendFpOtpViewModel(get()) }

    single<VerifyOtpRepository> { VerifyOtpRepositoryImpl(get()) }
    viewModel { VerifyFpOtpViewModel(get()) }

    single<FpChangePassRepository> { FpChangePassRepositoryImpl(get()) }
    viewModel { FpChangePassViewModel(get()) }

    single<GetAllPerformanceRepository> { GetAllPerformanceRepositoryImpl(get()) }
    viewModel { GetAllPerformanceViewModel(get()) }

    single<GetSubjectPerformanceRepository> { GetSubjectPerformanceRepositoryImpl(get()) }
    viewModel { GetSubjectPerformanceViewModel(get()) }

    single<GetBoardsRepository> { GetBoardsRepositoryImpl(get()) }
    viewModel { GetBoardsViewModel(get()) }

    single<GetTopicListRepository> { GetTopicListRepositoryImpl(get()) }
    viewModel { GetTopicListViewModel(get()) }

    single<GetAllClassRepository> { GetAllClassRepositoryImpl(get()) }
    viewModel { GetAllClassViewModel(get()) }

    single<GetPurchaseHistoryRepository> { GetPurchaseHistoryRepositoryImpl(get()) }
    viewModel { GetPurchaseHistoryViewModel(get()) }

    single<DownloadPurchaseReceiptRepository> { DownloadPurchaseReceiptRepositoryImpl(get()) }
    viewModel { GetPurchaseReceiptViewModel(get()) }

    single<GetTimeTableRepository> { GetTimeTableRepositoryImpl(get()) }
    viewModel { GetTimeTableViewModel(get()) }
}