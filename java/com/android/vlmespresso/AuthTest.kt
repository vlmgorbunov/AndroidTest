package com.android.vlmespresso

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(AuthActivity::class.java)

    @Test
    fun appLaunchedSuccessfully() {
        onView(withText("Войти")).check(matches(isDisplayed()))
    }

    @Test
    fun authScreenAppears() {
        emulateNfc()
        onView(withParentAndId(R.id.loginTextView, R.id.inputText)).check(matches(isDisplayed()))
        onView(withParentAndId(R.id.passwordTextView, R.id.inputText)).check(matches(isDisplayed()))
        onView(withText("Войти")).check(matches(isDisplayed()))
        onView(withText("test123@mail.ru")).check(matches(isDisplayed()))
        onView(withText("Зарегистрироваться")).check(matches(isDisplayed()))
    }

    @Test
    fun taggingWithoutInternetConnection() {
        enableWiFi(false)
        emulateNfc()
        onView(withParentAndId(R.id.loginTextView, R.id.inputText)).check(matches(isDisplayed()))
        onView(withParentAndId(R.id.passwordTextView, R.id.inputText)).check(matches(isDisplayed()))
        onView(withText("Войти")).check(matches(isDisplayed()))
        onView(withText("test123@mail.ru")).check(matches(isDisplayed()))
        onView(withText("Зарегистрироваться")).check(matches(isDisplayed()))
        enableWiFi(true)
    }

    private fun enableWiFi(enable: Boolean) {
        (activityRule.activity.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled = enable
    }

    @Test
    fun enterCorrectUsernameAndIncorrectPassword() {
        F_MESSAGES, msgs)
        onView(withParentAndId(R.id.loginTextView, R.id.inputText)).perform(typeText("test123@mail.ru"))
        onView(withParentAndId(R.id.passwordTextView, R.id.inputText)).perform(typeText("1234"))
        onView(withText("Войти")).check(matches(isDisplayed()))
        onView(withText("Войти")).perform(click())
        onView(withText("Неверный логин или пароль")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
    }

    @Test
    fun enterIncorrectUsernameAndPassword() {
        onView(withParentAndId(R.id.loginTextView, R.id.inputText)).perform(typeText("test123"))
        onView(withParentAndId(R.id.passwordTextView, R.id.inputText)).perform(typeText("1234"))
        onView(withText("Войти")).check(matches(isDisplayed()))
        onView(withText("Войти")).perform(click())
        onView(withText("Неверный логин или пароль")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
    }

    @Test
    fun enterEmptyUsernameAndPassword() {
        onView(withParentAndId(R.id.loginTextView, R.id.inputText)).perform(typeText(" "))
        onView(withParentAndId(R.id.passwordTextView, R.id.inputText)).perform(typeText(" "))
        onView(withText("Войти")).check(matches(isDisplayed()))
        onView(withText("Войти")).perform(click())
        onView(withText("Поля логин и пароль должны быть заполнены")).check(matches(isDisplayed()))
    }

    @Test
    fun enterCorrectUsernameAndEmptyPassword() {
        onView(withParentAndId(R.id.loginTextView, R.id.inputText)).perform(typeText("test123@mail.ru"))
        onView(withParentAndId(R.id.passwordTextView, R.id.inputText)).perform(typeText(" "))
        onView(withText("Войти")).check(matches(isDisplayed()))
        onView(withText("Войти")).perform(click())
        onView(withText("Поля логин и пароль должны быть заполнены")).check(matches(isDisplayed()))
    }

    @Test
    fun enterEmptyUsernameAndCorrectPassword() {
        onView(withParentAndId(R.id.loginTextView, R.id.inputText)).perform(typeText(" "))
        onView(withParentAndId(R.id.passwordTextView, R.id.inputText)).perform(typeText("12345678"))
        onView(withText("Войти")).check(matches(isDisplayed()))
        onView(withText("Войти")).perform(click())
        onView(withText("Поля логин и пароль должны быть заполнены")).check(matches(isDisplayed()))
    }

    @Test
    fun enterCorrectUsernameAndPasswordAndConfirmPin() {
        onView(withParentAndId(R.id.loginTextView, R.id.inputText)).perform(typeText("test123@mail.ru"))
        onView(withParentAndId(R.id.passwordTextView, R.id.inputText)).perform(typeText("12345678"))
        onView(withText("Войти")).check(matches(isDisplayed()))
        onView(withText("Войти")).perform(click())
        onView(withText("Загрузка данных")).check(matches(isDisplayed()))

        Thread.sleep(2000)

        try {
            onView(withText("Профиль")).check(matches(isDisplayed()))
            onView(withText("Главная")).perform(click())
        } catch (e: NoMatchingViewException) {
            Thread.sleep(2000)
            onView(withText("Ваш регион")).check(matches(isDisplayed()))
        }
    }
