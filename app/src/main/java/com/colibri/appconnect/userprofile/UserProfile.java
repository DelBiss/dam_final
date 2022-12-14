package com.colibri.appconnect.userprofile;

import android.widget.ImageView;

import com.colibri.appconnect.R;
import com.colibri.appconnect.data.entity.User;
import com.squareup.picasso.Picasso;

public class UserProfile {

    private String userId;
    private String displayName;
    private String primaryEmail;
    private String primaryPhoneNumber;
    private String avatar;
    private String title;
    private String location;



    public static UserProfile MockUserProfile(){
        return new UserProfile(
                "-1",
                "George Laraque",
                "george@laraque.com",
                "(514) 555-5555",
                "",
                "Contre-Maitre",
                "Chantier du CHUM"
        );
    }

    // TODO: 2022-08-10 Implement Constructor with UserDocument

    /**
     * No args constructor for use in serialization
     *
     */
    public UserProfile() {
    }

    public UserProfile(String userId,String displayName, String primaryEmail, String primaryPhoneNumber, String avatar, String title, String location) {
        super();
        this.userId = userId;
        this.displayName = displayName;
        this.primaryEmail = primaryEmail;
        this.primaryPhoneNumber = primaryPhoneNumber;
        this.avatar = avatar;
        this.title = title;
        this.location = location;
    }


    public UserProfile(User user) {
        this.userId = user.getId();
        this.displayName = user.getDisplayName();
        this.primaryEmail = user.getPrimaryEmail();
        this.primaryPhoneNumber = user.getPrimaryPhone();
        this.avatar = user.getAvatar();
        this.title = user.getTitle();
        this.location = user.getLocation();
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public String getPrimaryPhoneNumber() {
        return primaryPhoneNumber;
    }


    public void setAvatarToImageView(ImageView v){
        if (avatar != null && !avatar.isEmpty()) {
            Picasso.get().load(avatar).placeholder(R.drawable.user).error(R.drawable.user).into(v);
        }
    }
    public String getAvatar() {
        return avatar;
    }

    public String getTitle() {
        return title;
    }


    public String getLocation() {
        return location;
    }


}
