package org.totschnig.myexpenses.preference

import org.totschnig.myexpenses.MyApplication
import org.totschnig.myexpenses.R

// the following keys are stored as string resources, so that
// they can be referenced from preferences xml, and thus we
// can guarantee the referential integrity
enum class PrefKey(internal val resId: Int, internal val _key: String?) {
    CATEGORIES_SORT_BY_USAGES_LEGACY(R.string.pref_categories_sort_by_usages_key),
    SORT_ORDER_LEGACY(R.string.pref_sort_order_key),
    SORT_ORDER_TEMPLATES("sort_order_templates"),
    SORT_ORDER_CATEGORIES("sort_order_categories"),
    SORT_ORDER_ACCOUNTS("sort_order_accounts"),
    SORT_ORDER_BUDGET_CATEGORIES("sort_order_budget_categories"),
    PERFORM_SHARE(R.string.pref_perform_share_key),
    HELP(R.string.pref_help_key),
    SHARE_TARGET(R.string.pref_share_target_key),
    UI_THEME_KEY(R.string.pref_ui_theme_key),
    UI_FONTSIZE(R.string.pref_ui_fontsize_key),
    RESTORE(R.string.pref_restore_key),
    IMPORT_QIF(R.string.pref_import_qif_key),
    IMPORT_CSV(R.string.pref_import_csv_key),
    CONTRIB_PURCHASE(R.string.pref_contrib_purchase_key),
    LICENCE_LEGACY(R.string.pref_enter_licence_key),
    NEW_LICENCE(R.string.pref_new_licence_key),
    LICENCE_EMAIL("licence_email"),
    PROTECTION_LEGACY(R.string.pref_protection_password_key),
    SET_PASSWORD(R.string.pref_set_password_key),
    SECURITY_ANSWER(R.string.pref_security_answer_key),
    SECURITY_QUESTION(R.string.pref_security_question_key),
    PROTECTION_DELAY_SECONDS(R.string.pref_protection_delay_seconds_key),
    PROTECTION_ENABLE_ACCOUNT_WIDGET(R.string.pref_protection_enable_account_widget_key),
    PROTECTION_ENABLE_TEMPLATE_WIDGET(R.string.pref_protection_enable_template_widget_key),
    PROTECTION_ENABLE_BUDGET_WIDGET(R.string.pref_protection_enable_template_widget_key),
    PROTECTION_ENABLE_DATA_ENTRY_FROM_WIDGET(R.string.pref_protection_enable_data_entry_from_widget_key),
    EXPORT_FORMAT(R.string.pref_export_format_key),
    SEND_FEEDBACK(R.string.pref_send_feedback_key),
    MORE_INFO_DIALOG(R.string.pref_more_info_dialog_key),
    SHORTCUT_CREATE_TRANSACTION(R.string.pref_shortcut_create_transaction_key),
    SHORTCUT_CREATE_TRANSFER(R.string.pref_shortcut_create_transfer_key),
    SHORTCUT_CREATE_SPLIT(R.string.pref_shortcut_create_split_key),
    PLANNER_CALENDAR_ID(R.string.pref_planner_calendar_id_key),
    RATE(R.string.pref_rate_key),
    UI_LANGUAGE(R.string.pref_ui_language_key),
    APP_DIR(R.string.pref_app_dir_key),
    CATEGORY_PRIVACY(R.string.pref_category_privacy_key),
    CATEGORY_ADS(R.string.pref_category_ads_key),
    NO_ADS(R.string.pref_no_ads_key),
    CATEGORY_IO(R.string.pref_category_io_key),
    CATEGORY_BACKUP_RESTORE(R.string.pref_category_backup_restore_key),
    CATEGORY_SECURITY(R.string.pref_category_security_key),
    ACCOUNT_GROUPING(R.string.pref_account_grouping_key),
    PLANNER_CALENDAR_PATH("planner_calendar_path"),
    CURRENT_VERSION("currentversion"),
    FIRST_INSTALL_VERSION("first_install_version"),
    FIRST_INSTALL_DB_SCHEMA_VERSION("first_install_db_schema_version"),
    CURRENT_ACCOUNT("current_account"),
    PLANNER_LAST_EXECUTION_TIMESTAMP("planner_last_execution_timestamp"),
    AUTO_FILL_SWITCH(R.string.pref_auto_fill_key),
    AUTO_FILL_LEGACY("auto_fill"),
    AUTO_FILL_ACCOUNT(R.string.pref_auto_fill_account_key),
    AUTO_FILL_AMOUNT(R.string.pref_auto_fill_amount_key),
    AUTO_FILL_CATEGORY(R.string.pref_auto_fill_category_key),
    AUTO_FILL_COMMENT(R.string.pref_auto_fill_comment_key),
    AUTO_FILL_METHOD(R.string.pref_auto_fill_method_key),
    AUTO_FILL_DEBT(R.string.pref_auto_fill_debt_key),
    AUTO_FILL_FOCUS(R.string.pref_auto_fill_focus_key),
    AUTO_FILL_HINT_SHOWN("auto_fill_hint_shown"),
    TEMPLATE_CLICK_DEFAULT(R.string.pref_template_click_default_key),
    NEXT_REMINDER_RATE("nextReminderRate"),
    DISTRIBUTION_SHOW_CHART("distributionShowChart"),
    DISTRIBUTION_AGGREGATE_TYPES("distributionAggregateTypes"),
    MANAGE_STALE_IMAGES(R.string.pref_manage_stale_images_key),
    CSV_IMPORT_HEADER_TO_FIELD_MAP(R.string.pref_import_csv_header_to_field_map_key),
    CUSTOM_DECIMAL_FORMAT(R.string.pref_custom_decimal_format_key),
    CUSTOM_DATE_FORMAT(R.string.pref_custom_date_format_key),
    AUTO_BACKUP(R.string.pref_auto_backup_key),
    AUTO_BACKUP_TIME(R.string.pref_auto_backup_time_key),
    AUTO_BACKUP_DIRTY("auto_backup_dirty"),
    AUTO_BACKUP_CLOUD(R.string.pref_auto_backup_cloud_key),
    GROUP_WEEK_STARTS(R.string.pref_group_week_starts_key),
    GROUP_MONTH_STARTS(R.string.pref_group_month_starts_key),
    NEW_PLAN_ENABLED("new_plan_enabled"),
    INTERSTITIAL_LAST_SHOWN("interstitialLastShown"),
    ENTRIES_CREATED_SINCE_LAST_INTERSTITIAL("entriesCreatedSinceLastInterstitial"),
    NEW_ACCOUNT_ENABLED("new_account_enabled"),
    NEW_SPLIT_TEMPLATE_ENABLED("new_split_template_enabled"),
    SYNC_FREQUCENCY(R.string.pref_sync_frequency_key),
    SYNC_UPSELL_NOTIFICATION_SHOWN("sync_upsell_notification_shown"),
    MANAGE_SYNC_BACKENDS(R.string.pref_manage_sync_backends_key),
    TRACKING(R.string.pref_tracking_key),
    WEBDAV_TIMEOUT(R.string.pref_webdav_timeout_key),
    DEBUG_SCREEN(R.string.pref_debug_key),
    DEBUG_LOGGING(R.string.pref_debug_logging_key),
    SYNC_NOTIFICATION(R.string.pref_sync_notification_key),
    SYNC_WIFI_ONLY(R.string.pref_sync_wifi_only_key),
    DEBUG_ADS(R.string.pref_debug_show_ads_key),
    PROTECTION_DEVICE_LOCK_SCREEN(R.string.pref_protection_device_lock_screen_key),
    HISTORY_SHOW_BALANCE("history_show_balance"),
    HISTORY_SHOW_TOTALS("history_show_totals"),
    HISTORY_INCLUDE_TRANSFERS("history_include_transfers"),
    ROADMAP_VOTE("roadmap_vote"),
    ROADMAP_VERSION("roadmap_version"),
    CRASHREPORT_ENABLED(R.string.pref_crashreport_enabled_key),
    CRASHREPORT_USEREMAIL(R.string.pref_crashreport_useremail_key),
    CRASHLYTICS_USER_ID(R.string.pref_crashlytics_user_id_key),
    HOME_CURRENCY(R.string.pref_home_currency_key),
    LAST_ORIGINAL_CURRENCY("last_original_currency"),
    TRANSACTION_WITH_TIME(R.string.pref_transaction_time_key),
    TRANSACTION_WITH_VALUE_DATE(R.string.pref_value_date_key),
    TRANSACTION_LAST_ACCOUNT_FROM_WIDGET("transactionLastAccountFromWidget"),
    TRANSFER_LAST_ACCOUNT_FROM_WIDGET("transferLastAccountFromWidget"),
    TRANSFER_LAST_TRANSFER_ACCOUNT_FROM_WIDGET("transferLastTransferAccountFromWidget"),
    SPLIT_LAST_ACCOUNT_FROM_WIDGET("splitLastAccountFromWidget"),
    PROFESSIONAL_EXPIRATION_REMINDER_LAST_SHOWN("professionalExpirationReminderLastShown"),
    PERSONALIZED_AD_CONSENT(R.string.pref_ad_consent_key),
    SCROLL_TO_CURRENT_DATE(R.string.pref_scroll_to_current_date_key),
    EXPORT_PASSWORD(R.string.pref_security_export_password_key),
    CATEGORY_TRANSLATION(R.string.pref_category_translation_key),
    TRANSLATION(R.string.pref_translation_key),
    SYNC_CHANGES_IMMEDIATELY(R.string.pref_sync_changes_immediately_key),
    EXCHANGE_RATE_PROVIDER(R.string.pref_exchange_rate_provider_key),
    OPEN_EXCHANGE_RATES_APP_ID(R.string.pref_openexchangerates_app_id_key),
    PLANNER_EXECUTION_TIME(R.string.pref_plan_executor_time_key),
    WEBDAV_ALLOW_UNVERIFIED_HOST(R.string.pref_webdav_allow_unverified_host_key),
    CLONE_WITH_CURRENT_DATE(R.string.pref_clone_with_current_date_key),
    PLANNER_MANUAL_TIME(R.string.pref_planner_manual_time_key),
    OCR(R.string.pref_ocr_key),
    OCR_TOTAL_INDICATORS(R.string.pref_ocr_total_indicators_key),
    OCR_TIME_FORMATS(R.string.pref_ocr_time_formats_key),
    OCR_DATE_FORMATS(R.string.pref_ocr_date_formats_key),
    CRITERION_FUTURE(R.string.pref_criterion_future_key),
    FEATURE_UNINSTALL(R.string.pref_feature_uninstall_key),
    FEATURE_UNINSTALL_FEATURES(R.string.pref_feature_uninstall_features_key),
    FEATURE_UNINSTALL_LANGUAGES(R.string.pref_feature_uninstall_languages_key),
    EXPENSE_EDIT_SAVE_AND_NEW("expense_edit_save_and_new"),
    EXPENSE_EDIT_SAVE_AND_NEW_SPLIT_PART("expense_edit_save_and_new_split_part"),
    OCR_ENGINE(R.string.pref_ocr_engine_key),
    TESSERACT_LANGUAGE(R.string.pref_tesseract_language_key),
    MLKIT_SCRIPT(R.string.pref_mlkit_script_key),
    GROUP_HEADER(R.string.pref_group_header_show_details_key),
    UI_WEB(R.string.pref_web_ui_key),
    DATES_ARE_LINKED("dates_are_linked"),
    VOTE_REMINDER_LAST_CHECK("vote_reminder_last_check"),
    SAVE_TO_SYNC_BACKEND_CHECKED("save_to_sync_backend_checked"),
    NEWS(R.string.pref_news_key),
    DB_SAFE_MODE(R.string.pref_db_safe_mode_key),
    PARENT_CATEGORY_SELECTION_ON_TAP("parent_category_selection_on_tap"),
    PURGE_BACKUP(R.string.pref_purge_backup_key),
    PURGE_BACKUP_KEEP(R.string.pref_purge_backup_keep_key),
    PURGE_BACKUP_REQUIRE_CONFIRMATION(R.string.pref_purge_backup_require_confirmation_key),
    DEBUG_LOG_SHARE(R.string.pref_debug_logging_share_key),
    EXCHANGE_RATES_CLEAR_CACHE(R.string.pref_exchange_rates_clear_cache_key),
    DEBUG_REPAIR_987(R.string.pref_debug_repair_987_key),
    WEBUI_PASSWORD(R.string.pref_web_ui_password_key),
    CSV_EXPORT(R.string.pref_csv_export_key),
    CSV_EXPORT_SPLIT_CATEGORIES(R.string.pref_csv_export_split_categories_key),
    CSV_EXPORT_SPLIT_AMOUNT(R.string.pref_csv_export_split_amount_key),
    CSV_EXPORT_SPLIT_DATE_TIME(R.string.pref_csv_export_split_date_time_key),
    WEBUI_HTTPS(R.string.pref_web_ui_https_key),
    RUNNING_BALANCE(R.string.pref_running_balance_key),
    UI_ITEM_RENDERER_LEGACY(R.string.pref_ui_item_renderer_legacy_key),
    UI_ITEM_RENDERER_CATEGORY_ICON(R.string.pref_ui_item_renderer_category_icon_key),
    ENCRYPT_DATABASE("encrypt_database"),
    ENCRYPT_DATABASE_INFO(R.string.pref_encrypt_database_info_key),
    COIN_API_API_KEY(R.string.pref_coin_api_api_key_key),
    BACKUP_FILE_PREFIX(R.string.pref_backup_file_prefix_key),
    AUTO_BACKUP_UNENCRYPTED_INFO(R.string.pref_auto_backup_unencrypted_info_key),
    OPTIMIZE_PICTURE(R.string.pref_optimize_picture_key),
    OPTIMIZE_PICTURE_MAX_SIZE(R.string.pref_optimize_picture_max_size_key),
    OPTIMIZE_PICTURE_FORMAT(R.string.pref_optimize_picture_format_key),
    OPTIMIZE_PICTURE_QUALITY(R.string.pref_optimize_picture_quality_key),
    MANAGE_APP_DIR_FILES(R.string.pref_export_manage_files_key),
    ACCOUNT_PANEL_VISIBLE("account_panel_visible"),
    BANKING_FINTS(R.string.pref_banking_fints_key),
    COPY_ATTACHMENT(R.string.pref_copy_attachment_key),
    ATTACHMENT_MIME_TYPES(R.string.pref_attachment_mime_types_key),
    TRANLATION_IMPROVEMENT(R.string.pref_translation_improvement_key),
    UNMAPPED_TRANSACTION_AS_TRANSFER(R.string.pref_unmapped_transaction_as_transfer_key),
    DEFAULT_TRANSFER_CATEGORY(R.string.pref_default_transfer_category_key),
    PROTECTION_ALLOW_SCREENSHOT(R.string.pref_protection_allow_screenshot_key),
    CUSTOMIZE_MAIN_MENU(R.string.pref_customize_main_menu_key),
    REMOVE_LOCAL_CALENDAR(R.string.pref_remove_local_calendar_key),
    TIME_PICKER_INPUT_MODE("timePickerInputMode"),
    DATE_PICKER_INPUT_MODE("datePickerInputMode"),
    CAMERA_APP(R.string.pref_camera_app_key),
    CAMERA_CHOOSER(R.string.pref_camera_chooser_key),
    TRANSACTION_AMOUNT_COLOR_SOURCE(R.string.pref_transaction_amount_color_source_key),
    ;

    @Deprecated("")
    fun getKey(): String? {
        return if (resId == 0) _key else MyApplication.instance.getString(resId)
    }

    @Deprecated("")
    fun getString(defValue: String?): String? {
        return MyApplication.instance.settings.getString(getKey(), defValue)
    }

    constructor(resId: Int) : this(resId, null)

    constructor(key: String) : this(0, key)
}