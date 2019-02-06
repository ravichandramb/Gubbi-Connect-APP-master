package com.mygubbi.imaginestclientconnect.clientFeedback;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.models.APIResponse;
import com.mygubbi.imaginestclientconnect.models.ClientFeedBackResponse;
import com.mygubbi.imaginestclientconnect.models.ClientFeedbackQuestion;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;
import com.mygubbi.imaginestclientconnect.models.FeedbackOptions;
import com.mygubbi.imaginestclientconnect.models.FeedbackQuestion;
import com.mygubbi.imaginestclientconnect.models.FeedbackQuestionGroup;
import com.mygubbi.imaginestclientconnect.models.Option;
import com.mygubbi.imaginestclientconnect.rest.RestClientConnectClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

@SuppressLint("LogNotTimber")
public class ClientFeedbackPresenter {

    private static final String TAG = "ClientFeedbackPresenter";

    private ClientFeedbackView feedbackView;

    ClientFeedbackPresenter(ClientFeedbackView feedbackView) {
        this.feedbackView = feedbackView;
    }

    public void getFeedbackQuestions(String type) {
        Observable<List<FeedbackQuestion>> feedbackQuestionsObservable =
                RestClientConnectClient.getInstance()
                        .getApiService().getFeedbackQuestions(type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

        Observable<List<FeedbackOptions>> feedbackQuestionOptionsObservable =
                RestClientConnectClient.getInstance()
                        .getApiService().getFeedbackQuestionOptions(type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

        Observable<List<FeedbackQuestionGroup>> clientFeedbackObservable =
                Observable.zip(feedbackQuestionsObservable,
                        feedbackQuestionOptionsObservable, (feedbackQuestions, feedbackQuestionOptions) -> {
                            Map<String, List<Option>> optionsMap = new HashMap<>();
                            LinkedHashMap<String, List<ClientFeedbackQuestion>> questionMap = new LinkedHashMap<>();
                            List<FeedbackQuestionGroup> feedbackQuestionGroups = new ArrayList<>();

                            for (FeedbackOptions feedbackQuestionOption : feedbackQuestionOptions) {
                                optionsMap.put(feedbackQuestionOption.getQuestionCode(), feedbackQuestionOption.getOptions());
                            }

                            List<ClientFeedbackQuestion> feedbackQuestionList;

                            for (FeedbackQuestion feedbackQuestion : feedbackQuestions) {

                                feedbackQuestionList = questionMap.get(feedbackQuestion.getQuestionGroupTitle());

                                ClientFeedbackQuestion question = new ClientFeedbackQuestion(feedbackQuestion.getQuestionCode(),
                                        feedbackQuestion.getOptionType(),
                                        feedbackQuestion.getQuestionTitle(),
                                        optionsMap.get(feedbackQuestion.getQuestionCode()),feedbackQuestion.getQuestionGroupCode());

                                if (feedbackQuestionList == null) {
                                    feedbackQuestionList = new ArrayList<>();
                                }

                                feedbackQuestionList.add(question);

                                questionMap.put(feedbackQuestion.getQuestionGroupTitle(), feedbackQuestionList);
                            }

                            FeedbackQuestionGroup questionGroup;

                            short counter = 1;

                            for (String key : questionMap.keySet()) {
                                String questionText = String.valueOf(counter) + ". " + key;
                                questionGroup = new FeedbackQuestionGroup(questionText, ClientConnectConstants.VIEW_TYPE_QUESTION_HEADER);

                                ++counter;

                                feedbackQuestionGroups.add(questionGroup);

                                List<ClientFeedbackQuestion> questions = questionMap.get(key);

                                System.out.println(" ViewClientFeedbackQuestion----:"+questions);

                                for (ClientFeedbackQuestion question : questions) {
                                    questionGroup = new FeedbackQuestionGroup(question, ClientConnectConstants.VIEW_TYPE_QUESTION);

                                    feedbackQuestionGroups.add(questionGroup);
                                }
                            }

                            feedbackQuestionGroups.add(new FeedbackQuestionGroup("", ClientConnectConstants.VIEW_TYPE_SUBMIT));

                            return feedbackQuestionGroups;
                        });

        clientFeedbackObservable.subscribe(new Observer<List<FeedbackQuestionGroup>>() {
            @Override
            public void onSubscribe(Disposable d) {
                feedbackView.showProgress("Loading feedback questions...");
            }

            @Override
            public void onNext(List<FeedbackQuestionGroup> clientFeedbackList) {
                feedbackView.showFeedbackQuestion(clientFeedbackList);
                feedbackView.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                feedbackView.hideProgress();
                feedbackView.showFeedbackQuestion(new ArrayList<>());
            }

            @Override
            public void onComplete() {

            }
        });
    }


    public void getDsoFeedbackQuestions() {
        Observable<List<FeedbackQuestion>> feedbackQuestionsObservable =
                RestClientConnectClient.getInstance()
                        .getApiService().getDsoFeedbackQuestions()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

        Observable<List<FeedbackOptions>> feedbackQuestionOptionsObservable =
                RestClientConnectClient.getInstance()
                        .getApiService().getDsoFeedbackQuestionOptions()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

        Observable<List<FeedbackQuestionGroup>> clientFeedbackObservable =
                Observable.zip(feedbackQuestionsObservable,
                        feedbackQuestionOptionsObservable, (feedbackQuestions, feedbackQuestionOptions) -> {
                            Map<String, List<Option>> optionsMap = new HashMap<>();
                            LinkedHashMap<String, List<ClientFeedbackQuestion>> questionMap = new LinkedHashMap<>();
                            List<FeedbackQuestionGroup> feedbackQuestionGroups = new ArrayList<>();

                            for (FeedbackOptions feedbackQuestionOption : feedbackQuestionOptions) {
                                optionsMap.put(feedbackQuestionOption.getQuestionCode(), feedbackQuestionOption.getOptions());
                            }

                            List<ClientFeedbackQuestion> feedbackQuestionList;

                            for (FeedbackQuestion feedbackQuestion : feedbackQuestions) {
                                feedbackQuestionList = questionMap.get(feedbackQuestion.getQuestionGroupTitle());

                                ClientFeedbackQuestion question = new ClientFeedbackQuestion(feedbackQuestion.getQuestionCode(),
                                        feedbackQuestion.getOptionType(),
                                        feedbackQuestion.getQuestionTitle(),
                                        optionsMap.get(feedbackQuestion.getQuestionCode()),feedbackQuestion.getQuestionGroupCode());

                                if (feedbackQuestionList == null) {
                                    feedbackQuestionList = new ArrayList<>();
                                }

                                feedbackQuestionList.add(question);

                                questionMap.put(feedbackQuestion.getQuestionGroupTitle(), feedbackQuestionList);
                            }

                            FeedbackQuestionGroup questionGroup;

                            short counter = 1;

                            for (String key : questionMap.keySet()) {
                                String questionText = String.valueOf(counter) + ". " + key;
                                questionGroup = new FeedbackQuestionGroup(questionText, ClientConnectConstants.VIEW_TYPE_QUESTION_HEADER);

                                ++counter;

                                feedbackQuestionGroups.add(questionGroup);

                                List<ClientFeedbackQuestion> questions = questionMap.get(key);

                                System.out.println(" ViewClientDsoFeedbackQuestion----:"+questions);

                                for (ClientFeedbackQuestion question : questions) {
                                    questionGroup = new FeedbackQuestionGroup(question, ClientConnectConstants.VIEW_TYPE_QUESTION);

                                    feedbackQuestionGroups.add(questionGroup);
                                }
                            }

                            feedbackQuestionGroups.add(new FeedbackQuestionGroup("", ClientConnectConstants.VIEW_TYPE_SUBMIT));

                            return feedbackQuestionGroups;
                        });

        clientFeedbackObservable.subscribe(new Observer<List<FeedbackQuestionGroup>>() {
            @Override
            public void onSubscribe(Disposable d) {
                feedbackView.showProgress("Loading feedback questions...");
            }

            @Override
            public void onNext(List<FeedbackQuestionGroup> clientFeedbackList) {
                feedbackView.showFeedbackQuestion(clientFeedbackList);
                feedbackView.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                feedbackView.hideProgress();
                feedbackView.showFeedbackQuestion(new ArrayList<>());
            }

            @Override
            public void onComplete() {

            }
        });
    }


    public void submitUserFeedback(List<ClientFeedBackResponse> feedBackResponses) {
        System.out.println("test feedback data ::"+feedBackResponses);
        RestClientConnectClient.getInstance().getApiService()
                .submitFeedbackResponse(feedBackResponses)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        feedbackView.showProgress("Submitting feedback...");
                    }

                    @SuppressLint("LogNotTimber")
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String response = responseBody.string();
                            if (!TextUtils.isEmpty(response) && response.equalsIgnoreCase("succesfully inserted")) {
                                feedbackView.hideProgress();
                                updateProfile();
                            } else {
                                feedbackView.hideProgress();
                                feedbackView.showFeedbackSubmitted(false);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            feedbackView.hideProgress();
                            feedbackView.showFeedbackSubmitted(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        feedbackView.hideProgress();
                        feedbackView.showFeedbackSubmitted(false);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void submitUserDsoFeedback(List<ClientFeedBackResponse> feedBackResponses) {
        RestClientConnectClient.getInstance().getApiService()
                .submitDsoFeedbackResponse(feedBackResponses)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        feedbackView.showProgress("Submitting Dso feedback...");
                    }

                    @SuppressLint("LogNotTimber")
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String response = responseBody.string();
                            if (!TextUtils.isEmpty(response) && response.equalsIgnoreCase("succesfully inserted")) {
                                feedbackView.hideProgress();
                                updateProfile();
                            } else {
                                feedbackView.hideProgress();
                                feedbackView.showFeedbackSubmitted(false);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            feedbackView.hideProgress();
                            feedbackView.showFeedbackSubmitted(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        feedbackView.hideProgress();
                        feedbackView.showFeedbackSubmitted(false);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void updateProfile() {
        RestClientConnectClient.getInstance().getApiService()
                .updateProfile(ClientConnectApplication.getInstance().getOpportunityId(), "Yes")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<APIResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        feedbackView.showProgress("Updating profile...");
                    }

                    @SuppressLint("LogNotTimber")
                    @Override
                    public void onNext(APIResponse responseBody) {
                        if (responseBody != null ) {
                            if (responseBody.getStatus().equalsIgnoreCase("success")
                                    || responseBody.getStatus().equalsIgnoreCase("sucess")) {
                                ClientProfile profile = ClientConnectApplication.getInstance().getClientProfile();

                                if (profile != null) {
                                    profile.setFeedbackSubmitted("Yes");
                                    ClientConnectApplication.getInstance().setClientProfile(profile);
                                }

                                feedbackView.hideProgress();
                                feedbackView.showFeedbackSubmitted(true);
                            } else {
                                feedbackView.hideProgress();
                                feedbackView.showFeedbackSubmitted(false);
                            }
                        } else {
                            feedbackView.hideProgress();
                            feedbackView.showFeedbackSubmitted(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        feedbackView.hideProgress();
                        feedbackView.showFeedbackSubmitted(false);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}