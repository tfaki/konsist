package com.lemonappdev.konsist.api.provider.modifier

import com.lemonappdev.konsist.api.provider.KoBaseProvider

/**
 * An interface representing a Kotlin declaration that provides information about whether it has `crossinline` modifier.
 */
interface KoCrossInlineModifierProvider : KoBaseProvider, KoModifierProvider {
    /**
     * Whether the declaration has `crossinline` modifier.
     */
    val hasCrossInlineModifier: Boolean
}
