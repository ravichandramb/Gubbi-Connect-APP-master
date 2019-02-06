package com.mygubbi.imaginestclientconnect.clientFeedback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.helpers.GlideApp;
import com.mygubbi.imaginestclientconnect.models.ClientFeedbackQuestion;
import com.mygubbi.imaginestclientconnect.models.FeedbackQuestionGroup;
import com.mygubbi.imaginestclientconnect.models.Option;

import java.util.List;

import static com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants.VIEW_TYPE_QUESTION;
import static com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants.VIEW_TYPE_QUESTION_HEADER;
import static com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants.VIEW_TYPE_SUBMIT;
import static com.mygubbi.imaginestclientconnect.models.ClientFeedbackQuestion.TYPE_OPTION;
import static com.mygubbi.imaginestclientconnect.models.ClientFeedbackQuestion.TYPE_RATING;
import static com.mygubbi.imaginestclientconnect.models.ClientFeedbackQuestion.TYPE_TEXT;

public class FeedbackQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FeedbackQuestionAdapter";

    private Context context;
    private List<FeedbackQuestionGroup> dataList;
    private FeedbackInteractionListener listener;

    FeedbackQuestionAdapter(Context context, List<FeedbackQuestionGroup> dataList, FeedbackInteractionListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_QUESTION_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_header, parent, false);
            return new QuestionHeaderViewHolder(view);
        } else if (viewType == VIEW_TYPE_QUESTION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
            return new QuestionViewHolder(view);
        } else if (viewType == VIEW_TYPE_SUBMIT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback_submit, parent, false);
            return new FeedbackSubmitViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_QUESTION_HEADER:
                ((QuestionHeaderViewHolder) holder).bind(holder.getAdapterPosition());
                break;
            case VIEW_TYPE_QUESTION:
                ((QuestionViewHolder) holder).bind(holder.getAdapterPosition());
                break;
            case VIEW_TYPE_SUBMIT:
                ((FeedbackSubmitViewHolder) holder).bind();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getViewType();
    }

    class QuestionHeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView textQuestionHeader;

        QuestionHeaderViewHolder(View itemView) {
            super(itemView);

            textQuestionHeader = itemView.findViewById(R.id.text_header);
        }

        void bind(int position) {
            FeedbackQuestionGroup questionGroup = dataList.get(position);
            textQuestionHeader.setText(questionGroup.getQuestionHeader());
        }
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {

        private TextView textQuestion;
        private LinearLayout layoutOptions;
        private RadioGroup radioGroupRating;
        private EditText editText;

        private LayoutInflater inflater;
        private LinearLayout.LayoutParams layoutParams;
        private Animation scaledUpAnim, scaledDownAnim;

        QuestionViewHolder(View itemView) {
            super(itemView);

            textQuestion = itemView.findViewById(R.id.text_question);
            layoutOptions = itemView.findViewById(R.id.layout_options);
            radioGroupRating = itemView.findViewById(R.id.radio_group_rating);
            editText = itemView.findViewById(R.id.edit_text);

            inflater = LayoutInflater.from(context);

            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            scaledUpAnim = new ScaleAnimation(
                    1f, 1.15f, // Start and end values for the X axis scaling
                    1f, 1.15f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            scaledUpAnim.setFillAfter(true); // Needed to keep the result of the animation
            scaledUpAnim.setDuration(300);

            scaledDownAnim = new ScaleAnimation(
                    1f, 1f, // Start and end values for the X axis scaling
                    1f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            scaledDownAnim.setFillAfter(true); // Needed to keep the result of the animation
            scaledDownAnim.setDuration(100);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    onCommentsChanged(getAdapterPosition(), s.toString().trim());
                }
            });
        }

        void bind(int position) {
            FeedbackQuestionGroup questionGroup = dataList.get(position);

            ClientFeedbackQuestion feedbackQuestion = questionGroup.getFeedbackQuestion();

            switch (feedbackQuestion.getQuestionType()) {
                case TYPE_OPTION:
                    textQuestion.setVisibility(View.VISIBLE);
                    layoutOptions.setVisibility(View.VISIBLE);
                    radioGroupRating.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);

                    textQuestion.setText(feedbackQuestion.getQuestion());

                    List<Option> optionsList = feedbackQuestion.getOptions();

                    layoutOptions.removeAllViews();

                    for (int j = 0; j < optionsList.size(); j++) {
                        @SuppressLint("InflateParams") final View optionsView = inflater.inflate(R.layout.item_options, null);

                        ImageView imageOptionIcon = optionsView.findViewById(R.id.image_option_icon);
                        TextView textOption = optionsView.findViewById(R.id.text_option);

                        Option option = optionsList.get(j);

                        if (context != null) {
                            GlideApp.with(context)
                                    .load(option.getAnswerIcon())
                                    .error(R.drawable.ic_meh)
                                    .into(imageOptionIcon);
                        }

                        textOption.setText(option.getAnswerCode());

                        if (option.isUserAnswer()) {
                            imageOptionIcon.setBackgroundResource(R.drawable.ic_selected_option);
                            imageOptionIcon.startAnimation(scaledUpAnim);
                        } else {
                            imageOptionIcon.setBackgroundResource(0);
                            imageOptionIcon.startAnimation(scaledDownAnim);
                        }

                        if (j == 0) {
                            layoutParams.setMargins(25, 0, 0, 0);
                        } else {
                            layoutParams.setMargins(15, 0, 0, 0);
                        }
                        optionsView.setLayoutParams(layoutParams);

                        optionsView.setTag(option.getAnswerCode());

                        optionsView.setOnClickListener(v -> {
                            String response = v.getTag().toString();
                            onOptionClick(position, response);
                        });

                        layoutOptions.addView(optionsView);
                    }
                    break;
                case TYPE_RATING:
                    textQuestion.setVisibility(View.GONE);
                    layoutOptions.setVisibility(View.GONE);
                    radioGroupRating.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.GONE);

                    RadioButton radioButton;

                    for (int j = 0; j < 10; j++) {
                        RadioGroup.LayoutParams radioGroupParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT);
                        radioGroupParams.setMargins(10, 5, 0, 5);

                        radioButton = new RadioButton(context);
                        radioButton.setTag(j + 1);

                        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),
                                R.drawable.custom_round_radio_button, null);

                        radioButton.setButtonDrawable(null);
                        radioButton.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                        radioButton.setGravity(Gravity.CENTER);
                        radioButton.setLayoutParams(radioGroupParams);

                        radioButton.setText(String.valueOf(j + 1));

                        if (feedbackQuestion.getRating() == j + 1) {
                            radioButton.setChecked(true);
                        } else {
                            radioButton.setChecked(false);
                        }

                        radioGroupRating.addView(radioButton);
                    }

                    textQuestion.setText(feedbackQuestion.getQuestion());

                    radioGroupRating.setOnCheckedChangeListener((group, checkedId) -> {
                        int radioButtonID = radioGroupRating.getCheckedRadioButtonId();
                        View clickedRadioButton = radioGroupRating.findViewById(radioButtonID);
                        int rating = (int) clickedRadioButton.getTag();

                        onRatingClicked(position, rating);
                    });
                    break;
                case TYPE_TEXT:
                    textQuestion.setVisibility(View.GONE);
                    layoutOptions.setVisibility(View.GONE);
                    radioGroupRating.setVisibility(View.GONE);
                    editText.setVisibility(View.VISIBLE);
                    break;
            }
        }

        void onOptionClick(int position, String answerCode) {
            FeedbackQuestionGroup feedbackQuestionGroup = dataList.get(position);
            System.out.println("feedbackQuestionGroup ::-->"+feedbackQuestionGroup);
            ClientFeedbackQuestion feedbackQuestion = feedbackQuestionGroup.getFeedbackQuestion();

            List<Option> options = feedbackQuestion.getOptions();
            Option option;

            for (int i = 0; i < options.size(); i++) {
                option = options.get(i);
                if (option.getAnswerCode().equals(answerCode)) {
                    option.setUserAnswer(true);
                    options.set(i, option);
                } else {
                    option.setUserAnswer(false);
                    options.set(i, option);
                }
            }

            feedbackQuestion.setOptions(options);
            feedbackQuestionGroup.setFeedbackQuestion(feedbackQuestion);
            dataList.set(position, feedbackQuestionGroup);
            notifyItemChanged(position);
            notifyItemRangeChanged(position, 1);


            listener.onAnswered(feedbackQuestion.getQuestionCode(), answerCode,feedbackQuestion.getquestionGroupCode());
        }

        void onRatingClicked(int position, int rating) {
            FeedbackQuestionGroup feedbackQuestionGroup = dataList.get(position);
            ClientFeedbackQuestion feedbackQuestion = feedbackQuestionGroup.getFeedbackQuestion();

            feedbackQuestion.setRating(rating);

            feedbackQuestionGroup.setFeedbackQuestion(feedbackQuestion);
            dataList.set(position, feedbackQuestionGroup);

            listener.onAnswered(feedbackQuestion.getQuestionCode(), String.valueOf(rating),feedbackQuestion.getquestionGroupCode());
        }

        void onCommentsChanged(int position, String comment) {
            FeedbackQuestionGroup feedbackQuestionGroup = dataList.get(position);
            ClientFeedbackQuestion feedbackQuestion = feedbackQuestionGroup.getFeedbackQuestion();

            feedbackQuestion.setUserRemarks(comment);

            feedbackQuestionGroup.setFeedbackQuestion(feedbackQuestion);
            dataList.set(position, feedbackQuestionGroup);

            listener.onAnswered(feedbackQuestion.getQuestionCode(), comment,feedbackQuestion.getquestionGroupCode());
        }
    }

    class FeedbackSubmitViewHolder extends RecyclerView.ViewHolder {

        private Button buttonSubmit;

        FeedbackSubmitViewHolder(View itemView) {
            super(itemView);

            buttonSubmit = itemView.findViewById(R.id.button_submit);
        }

        void bind() {
            buttonSubmit.setOnClickListener(v -> listener.onSubmitClick());
        }
    }

    public interface FeedbackInteractionListener {

        void onAnswered(String questCode, String answerCode,String questionGroupCode);

        void onSubmitClick();
    }
}