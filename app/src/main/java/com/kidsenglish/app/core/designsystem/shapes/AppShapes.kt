package com.kidsenglish.app.core.designsystem.shapes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.kidsenglish.app.core.designsystem.dimensions.AppDimensions

/**
 * Centralized Material 3 shape definitions using soft, child-friendly corner radii
 * from [AppDimensions].
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(AppDimensions.RadiusSmall),
    small = RoundedCornerShape(AppDimensions.RadiusSmall),
    medium = RoundedCornerShape(AppDimensions.RadiusMedium),
    large = RoundedCornerShape(AppDimensions.RadiusLarge),
    extraLarge = RoundedCornerShape(AppDimensions.RadiusExtraLarge)
)
