package com.mygubbi.imaginestclientconnect.clientAssistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.helpers.GlideApp;
import com.mygubbi.imaginestclientconnect.models.ChatBotMessage;
import com.mygubbi.imaginestclientconnect.models.ClientAssist;
import com.mygubbi.imaginestclientconnect.models.NotificationData;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * @author Hemanth
 * @since 4/30/2018
 */
public class AssistantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ClientAssist> dataList;

    private String userName, userPhotoUrl;

    private static final int TYPE_BOT_MESSAGE = 1;
    private static final int TYPE_USER_MESSAGE = 2;
    private static final int TYPE_NOTIFICATION = 3;

    AssistantAdapter(Context context, ArrayList<ClientAssist> dataList) {
        this.context = context;
        this.dataList = dataList;

        SharedPreferences preferences = context.getSharedPreferences(ClientConnectConstants.CLIENT_CONNECT_PREFS,
                Context.MODE_PRIVATE);

        userName = preferences.getString(ClientConnectConstants.FIREBASE_USER_NAME, "");
        userPhotoUrl = preferences.getString(ClientConnectConstants.FIREBASE_USER_PHOTO, "");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_BOT_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_message, parent, false);
            return new BotChatViewHolder(view);
        } else if (viewType == TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, parent, false);
            return new UserChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assist_notification, parent, false);
            return new NotificationViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();

        switch (holder.getItemViewType()) {
            case TYPE_NOTIFICATION:
                NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;

                NotificationData notificationData = dataList.get(position).getNotificationData();

                notificationViewHolder.textContent.setText(notificationData.getBody());
                notificationViewHolder.textContent.setText(Html.fromHtml((notificationViewHolder.textContent.getText().toString()) +
                        " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;")); // 10 spaces
                notificationViewHolder.textTimeStamp.setText(convertTime(notificationData.getCreatedTs()));
                notificationViewHolder.textTitle.setText(notificationData.getTitle());
                if (notificationData.getImgUrl() != null && !notificationData.getImgUrl().isEmpty()) {
                    notificationViewHolder.imageNotification.setVisibility(View.VISIBLE);
                    GlideApp.with(context)
                            .load(notificationData.getImgUrl().trim())
                            .into(notificationViewHolder.imageNotification);
                } else {
                    notificationViewHolder.imageNotification.setVisibility(View.GONE);
                }

                break;
            case TYPE_BOT_MESSAGE:
                BotChatViewHolder botChatViewHolder = (BotChatViewHolder) holder;

                ChatBotMessage chatBotMessage = dataList.get(position).getChatBotMessage();

                botChatViewHolder.textContent.setText(chatBotMessage.getMessage());
                break;
            case TYPE_USER_MESSAGE:
                UserChatViewHolder userChatViewHolder = (UserChatViewHolder) holder;

                ChatBotMessage chatUserMessage = dataList.get(position).getChatBotMessage();

                userChatViewHolder.textContent.setText(chatUserMessage.getMessage());

                if (!TextUtils.isEmpty(userPhotoUrl)) {
                    userChatViewHolder.userImage.setVisibility(View.VISIBLE);
                    userChatViewHolder.textUserFirstLetter.setText("");
                    Glide.with(context).load(userPhotoUrl).into(userChatViewHolder.userImage);
                } else {
                    userChatViewHolder.userImage.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(userName)) {
                        userChatViewHolder.textUserFirstLetter.setText(userName.substring(0, 1));
                    }
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).isChat()
                ? dataList.get(position).getChatBotMessage().isByUser()
                ? TYPE_USER_MESSAGE : TYPE_BOT_MESSAGE : TYPE_NOTIFICATION;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle, textContent, textTimeStamp;
        private ImageView imageNotification;

        NotificationViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.text_title);
            textContent = itemView.findViewById(R.id.text_content);
            textTimeStamp = itemView.findViewById(R.id.text_timestamp);

            imageNotification = itemView.findViewById(R.id.image_notification);
        }
    }

    class BotChatViewHolder extends RecyclerView.ViewHolder {

        private TextView textContent;

        BotChatViewHolder(View itemView) {
            super(itemView);
            textContent = itemView.findViewById(R.id.text_content);
        }
    }

    class UserChatViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView textContent, textUserFirstLetter;

        UserChatViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.contact_image);
            textContent = itemView.findViewById(R.id.text_content);
            textUserFirstLetter = itemView.findViewById(R.id.tv_contact_image_fallback);
        }
    }

    private String convertTime(long time) {
        Date date = new Date(time);
        if (DateUtils.isToday(time)) {
            CharSequence timeSpanString = DateUtils.getRelativeTimeSpanString(time, new Date().getTime(), DateUtils.MINUTE_IN_MILLIS);
            return timeSpanString.toString();
        } else {
            Format format = new SimpleDateFormat("dd MMM yy, HH:mm a", Locale.getDefault());
            return format.format(date);
        }
    }

}