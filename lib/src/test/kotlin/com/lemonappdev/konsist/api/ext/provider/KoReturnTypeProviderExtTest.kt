package com.lemonappdev.konsist.api.ext.provider

import com.lemonappdev.konsist.api.KoKDocTag
import com.lemonappdev.konsist.api.provider.KoKDocProvider
import com.lemonappdev.konsist.api.provider.KoReturnTypeProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class KoReturnTypeProviderExtTest {
    private interface SampleTestReturnTypeDeclaration :
        KoReturnTypeProvider,
        KoKDocProvider

    @Test
    fun `hasValidKDocReturnTag() returns false when declaration not implement KoKDocProvider`() {
        // given
        val declaration: KoReturnTypeProvider = mockk {
            every { returnType } returns mockk()
            every { returnType?.name } returns "Boolean"
        }

        // when
        val sut = declaration.hasValidKDocReturnTag()

        // then
        sut shouldBeEqualTo false
    }

    @Test
    fun `hasValidKDocReturnTag() calls hasTags method`() {
        // given
        val declaration: SampleTestReturnTypeDeclaration = mockk {
            every { returnType } returns mockk()
            every { returnType?.name } returns "Boolean"
            every { kDoc?.hasTags(KoKDocTag.RETURN) } returns true
        }

        // when
        declaration.hasValidKDocReturnTag()

        // then
        verify { declaration.kDoc?.hasTags(KoKDocTag.RETURN) }
    }
}
