package com.lemonappdev.konsist.core.declaration

import com.lemonappdev.konsist.api.declaration.KoPropertyDeclaration
import com.lemonappdev.konsist.api.provider.KoParentProvider
import com.lemonappdev.konsist.core.cache.KoDeclarationCache
import com.lemonappdev.konsist.core.provider.KoAnnotationDeclarationProviderCore
import com.lemonappdev.konsist.core.provider.KoDeclarationFullyQualifiedNameProviderCore
import com.lemonappdev.konsist.core.provider.KoDelegateProviderCore
import com.lemonappdev.konsist.core.provider.KoExplicitTypeProviderCore
import com.lemonappdev.konsist.core.provider.KoExtensionProviderCore
import com.lemonappdev.konsist.core.provider.KoModifierProviderCore
import com.lemonappdev.konsist.core.provider.KoPackageDeclarationProviderCore
import com.lemonappdev.konsist.core.provider.KoReceiverTypeProviderCore
import com.lemonappdev.konsist.core.provider.KoResideInOrOutsidePackageProviderCore
import com.lemonappdev.konsist.core.provider.KoTopLevelProviderCore
import com.lemonappdev.konsist.core.provider.KoVarAndValProviderCore
import org.jetbrains.kotlin.psi.KtAnnotated
import org.jetbrains.kotlin.psi.KtCallableDeclaration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTypeParameterListOwner

internal class KoPropertyDeclarationImpl private constructor(
    override val ktProperty: KtProperty,
    override val parentDeclaration: KoParentProvider?,
) :
    KoPropertyDeclaration,
    KoBaseDeclarationImpl(ktProperty),
    KoAnnotationDeclarationProviderCore,
    KoPackageDeclarationProviderCore,
    KoResideInOrOutsidePackageProviderCore,
    KoDeclarationFullyQualifiedNameProviderCore,
    KoModifierProviderCore,
    KoTopLevelProviderCore,
    KoVarAndValProviderCore,
    KoExtensionProviderCore,
    KoReceiverTypeProviderCore,
    KoDelegateProviderCore,
    KoExplicitTypeProviderCore {
    override val ktAnnotated: KtAnnotated
        get() = ktProperty

    override val ktFile: KtFile?
        get() = null

    override val ktTypeParameterListOwner: KtTypeParameterListOwner
        get() = ktProperty

    override val ktCallableDeclaration: KtCallableDeclaration
        get() = ktProperty

    override val delegateName: String? by lazy {
        ktProperty
            .delegateExpression
            ?.text
            ?.replace("\n", " ")
            ?.substringAfter("by ")
            ?.substringBefore("{")
            ?.removeSuffix(" ")
    }

    internal companion object {
        private val cache: KoDeclarationCache<KoPropertyDeclaration> = KoDeclarationCache()

        internal fun getInstance(ktProperty: KtProperty, parentDeclaration: KoParentProvider?): KoPropertyDeclaration =
            cache.getOrCreateInstance(ktProperty, parentDeclaration) {
                KoPropertyDeclarationImpl(ktProperty, parentDeclaration)
            }
    }
}
