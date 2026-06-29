package com.davidnardie.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import com.davidnardie.pokemon.domain.repository.PokemonRepository
import com.davidnardie.pokemon.domain.repository.SettingsRepository
import org.koin.mp.KoinPlatform.getKoin
import kotlin.reflect.KClass

class IosViewModelStoreOwner : ViewModelStoreOwner {
    private val store = ViewModelStore()
    override val viewModelStore: ViewModelStore get() = store

    /**
     * Clears all ViewModels scoped to this owner. Called from Swift wrapper `deinit`
     * (e.g. `PokemonListViewModelWrapper` in iosApp) so coroutines in `viewModelScope`
     * are cancelled when a SwiftUI screen is dismissed.
     *
     * Android Studio may report this as unused: it is only referenced from Swift in `iosApp/`,
     * which the IDE does not analyze as Kotlin usage. On Android/Compose, lifecycle is handled
     * by `koinViewModel()` and the activity/compose ViewModelStore instead.
     */
    fun clear() {
        viewModelStore.clear()
    }
}

private inline fun <reified T : ViewModel> IosViewModelStoreOwner.viewModel(
    key: String? = null,
    crossinline creator: () -> T,
): T {
    val factory = object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: KClass<VM>, extras: CreationExtras): VM {
            @Suppress("UNCHECKED_CAST")
            return creator() as VM
        }
    }
    val provider = ViewModelProvider.create(viewModelStore, factory, CreationExtras.Empty)
    return if (key != null) {
        provider[key, T::class]
    } else {
        provider[T::class]
    }
}

/**
 * Resolves a [PokemonListViewModel] scoped to this store owner.
 *
 * Used from Swift (`PokemonListViewModelWrapper` in `iosApp/`), not from Kotlin in this repo.
 * Android Studio shows no usages because call sites live in Swift/Xcode; Android uses
 * `koinViewModel<PokemonListViewModel>()` in Compose (`PokemonMainListScreen`).
 */
fun IosViewModelStoreOwner.pokemonListViewModel(): PokemonListViewModel =
    viewModel {
        PokemonListViewModel(repository = getKoin().get<PokemonRepository>())
    }

/**
 * Resolves a [SettingsViewModel] scoped to this store owner.
 *
 * Used from Swift (`SettingsViewModelWrapper` in `iosApp/`). Unused in Android Studio for the
 * same reason as [pokemonListViewModel]: Swift-only entry points. Android uses
 * `koinViewModel<SettingsViewModel>()` in `App.kt`.
 */
fun IosViewModelStoreOwner.settingsViewModel(): SettingsViewModel =
    viewModel {
        SettingsViewModel(settingsRepository = getKoin().get<SettingsRepository>())
    }

/**
 * Resolves a [PokemonDetailViewModel] for [pokemonId], scoped to this store owner.
 *
 * Used from Swift (`PokemonDetailViewModelWrapper` / `PokemonDetailContainerView` in `iosApp/`).
 * Android Studio does not see those references. On Android, the detail screen uses
 * `koinViewModel(key = "detail-$id", parameters = { parametersOf(id) })` in Compose.
 */
fun IosViewModelStoreOwner.pokemonDetailViewModel(pokemonId: Int): PokemonDetailViewModel =
    viewModel(key = "detail-$pokemonId") {
        PokemonDetailViewModel(
            pokemonId = pokemonId,
            repository = getKoin().get<PokemonRepository>(),
            settingsRepository = getKoin().get<SettingsRepository>(),
        )
    }
