package com.aarafrao.test.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import com.aarafrao.test.R
import com.aarafrao.test.model.ItemModelItem
import com.aarafrao.test.network.ServiceBuilder
import com.aarafrao.test.repository.Repository
import com.aarafrao.test.ui.theme.TestTheme
import com.aarafrao.test.viewModel.ListViewModel
import com.aarafrao.test.viewModel.ViewModelFactory

//TODO Description:
//Build a simple app to hit the Popular Fashion Products API and show a list of Products,
//that shows details when items on the list are tapped (a typical master/detail app).
//We'll be using the following API.
//https://fakestoreapiserver.reactbd.com/smart
//Following is the figma link for the reference design
//https://www.figma.com/design/8K9wI08QwaKdsHA3MqZUXK/Test-Design

//Developed code should be pushed to GitHub
//What we care about:
//• Using Kotlin and Jetpack Compose
//• Good Architecture e.g. MVVM, etc.
//• Code to be generic and simple
//• leverage today's best coding practices
//Create a GitHub repository, ensure the name is generic and doesn’t have any company
//names. Commit your code to the GitHub repository and share the link with us. Only share a
//link, do not send the actual code files


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val repository = Repository(ServiceBuilder.apiService)

            val listViewModel: ListViewModel = ViewModelProvider(
                this,
                ViewModelFactory(repository)
            )[ListViewModel::class.java]

            var selectedProduct by remember { mutableStateOf<ItemModelItem?>(null) }

            if (selectedProduct == null) {
                TestTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        "The Fashion Design",
                                        color = Color.White,
                                        style = TextStyle(
                                            fontWeight = FontWeight.W500,
                                            fontSize = 22.sp
                                        )
                                    )
                                },
                                actions = {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Menu",
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = "Menu",
                                        modifier = Modifier.padding(10.dp),
                                    )
                                },
                                navigationIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Menu",
                                        modifier = Modifier.padding(10.dp),
                                    )
                                },
                                colors = TopAppBarColors(
                                    containerColor = colorResource(id = R.color.theme_orange),
                                    titleContentColor = Color.Black,
                                    actionIconContentColor = Color.White,
                                    navigationIconContentColor = Color.White,
                                    scrolledContainerColor = Color.White
                                )
                            )
                        }) { innerPadding ->

                        ProductListScreen(listViewModel, innerPadding) { product ->
                            selectedProduct = product
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun ProductListScreen(
    listViewModel: ListViewModel,
    innerPadding: PaddingValues,
    onProductClick: (ItemModelItem) -> Unit,
) {
    val products = listViewModel.products.collectAsState()
    Log.d("TAG", "ProductListScreen: ${products.value}")

    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(products.value) { product ->
            ProductItem(product = product, onClick = { onProductClick(product) })
        }
    }
}

@Composable
fun ProductItem(product: ItemModelItem, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = rememberImagePainter(data = product.image),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontWeight = FontWeight.W400,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = product.title,
                            style = TextStyle(
                                fontWeight = FontWeight.W400,
                                fontSize = 14.sp
                            ),
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = product.category,
                            style = TextStyle(
                                fontWeight = FontWeight.W400,
                                fontSize = 14.sp
                            ),
                            color = Color.Gray
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Goto",
                tint = Color.Gray,
                modifier = Modifier.padding(10.dp),
            )
        }
        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(bottom = 10.dp, top = 10.dp))
    }
}

