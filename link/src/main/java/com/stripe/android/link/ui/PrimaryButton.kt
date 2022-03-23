package com.stripe.android.link.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.stripe.android.link.theme.HorizontalPadding

internal enum class PrimaryButtonState {
    Enabled,
    Disabled,
    Processing
}

@Composable
internal fun PrimaryButton(
    label: String,
    state: PrimaryButtonState,
    @DrawableRes icon: Int? = null,
    onButtonClick: () -> Unit
) {
    val isEnabled = state == PrimaryButtonState.Enabled

    CompositionLocalProvider(
        LocalContentAlpha provides if (isEnabled) ContentAlpha.high else ContentAlpha.disabled,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isEnabled,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    disabledBackgroundColor = MaterialTheme.colors.primary
                )
            ) {
                if (state == PrimaryButtonState.Processing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = label,
                        color = MaterialTheme.colors.onPrimary
                            .copy(alpha = LocalContentAlpha.current)
                    )
                }
            }
            if (icon != null && state != PrimaryButtonState.Processing) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .height(16.dp)
                        // width should be 13dp and must include the horizontal padding
                        .width(13.dp + 40.dp)
                        .padding(horizontal = HorizontalPadding),
                    tint = MaterialTheme.colors.onPrimary.copy(alpha = LocalContentAlpha.current)
                )
            }
        }
    }
}
