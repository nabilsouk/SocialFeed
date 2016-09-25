package com.internship.socialfeed.components;

import org.json.JSONException;
import org.json.JSONObject;

;
public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private Provider provider;
    public User(String email,String password) {
        this.id =String.valueOf((int) (Math.random()*100));
        this.email = email;
        this.password = password;
        provider=null;
    }

    public User(String name, String email, String password) {
        this.id =String.valueOf((int) (Math.random()*100));
        this.name = name;
        this.email = email;
        this.password = password;
        provider=null;
    }

    public User(JSONObject userJSON) {
        try {
            if(!userJSON.isNull("id")) this.id = userJSON.getString("id");
            if (!userJSON.isNull("name")) this.name = userJSON.getString("name");
            if(!userJSON.isNull("email")) this.email = userJSON.getString("email");
            if(!userJSON.isNull("password")) this.password = userJSON.getString("password");
            if(!userJSON.isNull("provider")) this.provider = Enum.valueOf(Provider.class,userJSON.getString("provider"));
        } catch(JSONException e) {

        }
    }
    public void setProvider(Provider provider){
        this.provider = provider;
    }
    public Provider getProvider(){
        return provider;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (name == null)
        return "id "+id+"Email: "+email+" Password: "+password;
        else return "Name: "+name+" Email: "+email+" Password: "+password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toJSONString() {
        return toJSON().toString();
    }

    /**
     *
     */
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            if (id != null) jsonObject.put("id", id);
            if (name != null)  jsonObject.put("name", name);
            if (email != null) jsonObject.put("email", email);
            if (password != null) jsonObject.put("password", password);
            if (provider!=null) jsonObject.put("provider", provider.toString());
        } catch (JSONException e) {
            // nothing to be done
        }
        return jsonObject;
    }

}
