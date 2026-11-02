package com.kidsenglish.app.core.designsystem.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.kidsenglish.app.core.designsystem.dimens.Dimens

/**
 * Friendly, rounded Material 3 shape definitions.
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(Dimens.RadiusXS),
    small = RoundedCornerShape(Dimens.RadiusS),
    medium = RoundedCornerShape(Dimens.RadiusM),
    large = RoundedCornerShape(Dimens.RadiusL),
    extraLarge = RoundedCornerShape(Dimens.RadiusXL)
)
