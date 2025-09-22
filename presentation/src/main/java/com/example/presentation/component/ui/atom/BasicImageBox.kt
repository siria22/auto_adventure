package com.example.presentation.component.ui.atom

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.presentation.R

/**
 * Displays an image with a circular progress indicator while it is loading.
 * Use AsyncImage from Coil.
 *
 * @param modifier The modifier to apply to this composable.
 * @param size The size of the image box. The default value is 128.dp.
 * @param galleryUri The [Uri] of the image to display. If null, a gray box will be displayed instead.
 */
@Composable
fun BasicImageBox(
    modifier: Modifier = Modifier,
    size: Dp = 128.dp,
    galleryUri: Uri?,
) {
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (galleryUri == null) {
            isLoading = false
            Box(
                modifier = modifier
                    .background(Color.Gray)
                    .clip(RoundedCornerShape(4.dp))
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(galleryUri)
                    .crossfade(true)
                    .listener(
                        onSuccess = { _, _ -> isLoading = false },
                        onError = { _, _ -> isLoading = false }
                    )
                    .error(R.drawable.broken_image)
                    .build(),
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(4.dp)),
                contentDescription = "이미지 콘텐츠",
                contentScale = ContentScale.Crop
            )
        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}
