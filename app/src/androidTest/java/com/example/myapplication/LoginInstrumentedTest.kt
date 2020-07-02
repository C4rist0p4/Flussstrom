package com.example.myapplication

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

class LoginInstrumentedTest() {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    private val name = "asoe"
    private val email = "asoe@asoe.de"
    private val password = "garf"

    @Test
    fun test_Login_Toast() {

        try {
            onView(withId(R.id.login_main)).check(matches(isDisplayed()))
        } catch (ignore: NoMatchingViewException) {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
            onView(withText("Log Out")).perform(click())
        }
        onView(withId(R.id.nameET)).perform(ViewActions.typeText(name))
        onView(withId(R.id.emailET)).perform(ViewActions.typeText(email))
        onView(withId(R.id.password1ET)).perform(ViewActions.typeText(password), ViewActions.pressKey(KeyEvent.KEYCODE_BACK))

        onView(withId(R.id.loginBt)).perform(click())

        onView(withText("Ihr Passwort muss mindestens 6 Zeichen lang sein."))
                .inRoot(ToastMatcher()).check(matches(isDisplayed()))

    }
}