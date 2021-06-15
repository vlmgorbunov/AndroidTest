package com.android.vlmespresso

import android.content.res.Resources
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`

class AuthMatcher(
    private val parentViewIdMatcher: Matcher<Int>,
    private val childViewIdMatcher: Matcher<Int>
) : org.hamcrest.TypeSafeMatcher<View>() {

    private var resources: Resources? = null

    override fun describeTo(description: Description) {
        var idDescription = parentViewIdMatcher.toString().replace("\\D+".toRegex(), "")
        val id = Integer.parseInt(idDescription)
        if (resources != null) {
            idDescription = try {
                resources!!.getResourceName(id)
            } catch (e: Resources.NotFoundException) {
                // No big deal, will just use the int value.
                String.format("%s (resource name not found)", idDescription)
            }
        }
        description.appendText("with id: $idDescription")
    }

    public override fun matchesSafely(view: View): Boolean {
        resources = view.resources
        return childViewIdMatcher.matches(view.id) && parentViewIdMatcher.matches((view.parent.parent.parent as View).id)
    }
}

fun withParentAndId(parentId: Int, childId: Int): Matcher<View> {
    return withParentAndId(`is`<Int>(parentId), `is`<Int>(childId))
}

fun withParentAndId(parentViewIdMatcher: Matcher<Int>, childViewIdMatcher: Matcher<Int>): Matcher<View> {
    return AuthMatcher(parentViewIdMatcher, childViewIdMatcher)
}