package com.example.playersquiz.remote.models

data class RootMetadataSupportedLocaleRemoteModel (
    val appLanguage: RootMetadataAppPlatformRemoteModel? = null,
    val localeIdentifier: String? = null,
    val languageIdentifier: Long? = null,
    val localeBasedPointOfSaleName: String? = null,
    val appInfoURL: String? = null,
    val createAccountMarketingText: String? = null,
    val forgotPasswordURL: String? = null,
    val appSupportURLs: RootMetadataAppPlatformRemoteModel? = null,
    val bookingSupportURL: String? = null,
    val contactUsSupportArticleURL: String? = null,
    val websiteURL: String? = null,
    val accountURL: String? = null,
    val termsOfBookingLinkText: String? = null,
    val termsAndConditionsURL: String? = null,
    val termsOfBookingURL: String? = null,
    val learnAboutSortAndFilterURL: String? = null,
    val privacyPolicyURL: String? = null,
    val resultsSortFAQLegalLink: String? = null,
    val loyaltyTermsAndConditionsURL: String? = null,
    val coronavirusInfoUrl: String?
)