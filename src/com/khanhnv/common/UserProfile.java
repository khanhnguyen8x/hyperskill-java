package com.khanhnv.common;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class UserProfile implements Serializable {
    private static final long serialVersionUID = 26292552485L;

    private String login;
    private String email;
    private transient String password;

    public UserProfile(String login, String email, String password) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    // implement readObject and writeObject properly
    // transient and non-transient fields
    private void writeObject(ObjectOutputStream oos) throws Exception {
        // write the custom serialization code here
        oos.defaultWriteObject();
        String encryptPassword = encrypt(password);
        oos.writeObject(encryptPassword);
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        // write the custom deserialization code here
        ois.defaultReadObject();
        this.password = decrypt((String) ois.readObject());
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    private String encrypt(String password) {
        char[] result = new char[password.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (char) (password.charAt(i) + 1);
        }
        return new String(result);
    }

    private String decrypt(String encrypt) {
        char[] result = new char[encrypt.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (char) (encrypt.charAt(i) - 1);
        }
        return new String(result);
    }

    public static void main(String[] args) {
        var user = new UserProfile("a", "b", "123456");
        var encrypt = user.encrypt("123456");
        var password = user.decrypt(encrypt);
        System.out.println(encrypt);
        System.out.println(password);
    }
}