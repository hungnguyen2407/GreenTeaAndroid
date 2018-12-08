package com.greentea.model;

public enum NameOfResources {

    USER_ID("user_id"),
    USER_NAME("user_name"),
    USER_FACULTY_ID("user_faculty_id"),
    USER_FACULTY_NAME("user_faculty_name"),
    USER_CLASS_ID("user_class_id"),
    USER_CLASS_NAME("user_class_name"),
    LOGIN_SUCCESS("login_success"),
    ANNOUNCE_DATA("announce_data"),
    GROUP_JOIN_MESSAGE("group_join_message"),
    GROUP_LIST("group_list"),
    POST_ANNOUNCE_MESSAGE("post_announce_message"),
    USER_INFO("user_info"),
    RESET_PASS_MESSAGE("reset_pass_message"),
    CHANGE_PASS_MESSAGE("change_pass_message"),
    ANNOUNCE_HEADER("announce_header"),
    ANNOUNCE_CONTENT("announce_content"),
    ANNOUNCE_DATE("announce_date"),
    ANNOUNCE_ID("announce_id"),
    GROUP_ID("group_id"),
    GROUP_NAME("group_name"),
    GROUP_MEM_NUM("group_mem_num"),
    GROUP_FACULTY_ID("group_faculty_id"),
    DELETE_ANNOUNCE_MESSAGE("delete_announce_message"),
    USER_LEVEL("user_level");
    NameOfResources(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String toString() {
        return name;
    }
}
