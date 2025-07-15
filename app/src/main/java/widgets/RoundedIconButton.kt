package widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val IconButtonSizeModifier = Modifier.size(40.dp)
@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    imageVector : ImageVector,
    onClick : () -> Unit,
    tint : Color = Color.Black.copy(alpha = 0.8f),
    cardBackgroundColor: Color = MaterialTheme.colorScheme.background,
    cardElevation : Dp = 4.dp
){
    Card(modifier = modifier
        .padding(4.dp)
        .clickable{onClick.invoke()}
        .then(IconButtonSizeModifier),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation))
    {
        Box(
            modifier = Modifier.size(40.dp), // Ensure Box fills the Card's content area
            contentAlignment = Alignment.Center // Center its content (the Icon)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "Plus or Minus icon button",
                tint = tint
            )
        }
    }
}