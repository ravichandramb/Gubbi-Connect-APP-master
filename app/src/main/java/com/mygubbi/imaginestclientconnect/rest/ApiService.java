package com.mygubbi.imaginestclientconnect.rest;

import com.google.gson.JsonObject;
import com.mygubbi.imaginestclientconnect.models.APIResponse;
import com.mygubbi.imaginestclientconnect.models.AccessToken;
import com.mygubbi.imaginestclientconnect.models.ChatBotMessage;
import com.mygubbi.imaginestclientconnect.models.ClientDocument;
import com.mygubbi.imaginestclientconnect.models.ClientFeedBackResponse;
import com.mygubbi.imaginestclientconnect.models.ClientHandoverResponse;
import com.mygubbi.imaginestclientconnect.models.ClientIssue;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;
import com.mygubbi.imaginestclientconnect.models.ClientProject;
import com.mygubbi.imaginestclientconnect.models.FeedbackOptions;
import com.mygubbi.imaginestclientconnect.models.FeedbackQuestion;
import com.mygubbi.imaginestclientconnect.models.NotificationList;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Hemanth
 * @since 4/6/2018
 */
public interface ApiService {

    @POST("MyGubbiAuth/oauth/token")
    Observable<String> login(@Body JsonObject jsonObject);

    @FormUrlEncoded
    @POST("MyGubbiAuth/oauth/token")
    Observable<AccessToken> getOAuthToken(@Header("Authorization") String authorization,
                                          @Field("username") String username, @Field("password") String password,
                                          @Field("grant_type") String grant_type);

    @FormUrlEncoded
    @POST("MyGubbiAuth/oauth/token")
    Call<AccessToken> synchronousTokenRefresh(@Header("Authorization") String authorization,
                                              @Field("username") String username, @Field("password") String password,
                                              @Field("grant_type") String grant_type);

    @POST("mygubbi/cca/getprofile")
    Observable<ClientProfile> getClientProfile(@Query("opportunity_id") String opportunity_id);

    @POST("mygubbi/cca/getdocuments")
    Observable<List<ClientDocument>> getClientDocumentsList(@Query("opportunity_id") String opportunity_id);

    @POST("mygubbi/cca/getcustomerissues")
    Observable<List<ClientIssue>> getClientSupportList(@Query("opportunity_id") String opportunity_id);

    @POST("mygubbi/cca/getupdates")
    Observable<List<ClientProject>> getClientProjectUpdates(@Query("opportunity_id") String opportunity_id);

    @POST("mygubbi/cca/gethandoverdetails")
    Observable<List<ClientHandoverResponse>> getHandoverDetails(@Query("opportunity_id") String opportunity_id);

    @POST("mygubbi/cca/createissue")
    Observable<APIResponse> createClientSupport(@Body RequestBody requestBody);

    @GET("mygubbi/cca/getfeedbackquestions/{type}")
    Observable<List<FeedbackQuestion>> getFeedbackQuestions(@Path("type") String type);


    @GET("mygubbi/cca/getfeedbackquestionoption/{type}")
    Observable<List<FeedbackOptions>> getFeedbackQuestionOptions(@Path("type") String type);

    @POST("mygubbi/cca/addfeedbackquestionanswer")
    Observable<ResponseBody> submitFeedbackResponse(@Body List<ClientFeedBackResponse> feedBackResponses);


    @GET("mygubbi/cca/getdsofeedbackquestions")
    Observable<List<FeedbackQuestion>> getDsoFeedbackQuestions();

    @GET("mygubbi/cca/getdsofeedbackquestionoption")
    Observable<List<FeedbackOptions>> getDsoFeedbackQuestionOptions();

    @POST("mygubbi/cca/adddsofeedbackquestionanswer")
    Observable<ResponseBody> submitDsoFeedbackResponse(@Body List<ClientFeedBackResponse> feedBackResponses);



    @POST("MyGubbiApi/notification/get/user-notification/{userid}/{pageno}/{itemPage}")
    Observable<NotificationList> getUserNotifications(@Path("userid") String userId,
                                                      @Path("pageno") int pageNumber,
                                                      @Path("itemPage") int itemsPerPage);

    @POST("MyGubbiApi/chatbot/get/answer")
    Observable<ChatBotMessage> queryBot(@Body JsonObject queryObject);

    @POST("mygubbi/cca/updateprofile")
    Observable<APIResponse> updateProfile(@Query("opportunity_id") String opportunity_id,
                                          @Query("feedbackSubmitted") String feedbackSubmitted);

    @POST("mygubbi/cca/uploaddocument")
    Observable<APIResponse> uploadHandoverFile(@Body RequestBody requestBody);

}