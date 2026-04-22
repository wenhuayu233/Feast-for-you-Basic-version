package com.tqwc.feastweb.service.impl;

import com.tqwc.feastcommon.entity.Dish;
import com.tqwc.feastcommon.entity.Favorite;
import com.tqwc.feastweb.service.DishService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private DishService dishService;

    @Spy
    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Test
    void addFavorite_shouldThrowWhenDishNotExists() {
        Long dishId = 1L;
        Long userId = 2L;
        when(dishService.getActiveById(dishId)).thenReturn(null);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> favoriteService.addFavorite(dishId, userId));

        assertEquals("菜品不存在或已下架", exception.getMessage());
        verify(favoriteService, never()).save(any(Favorite.class));
    }

    @Test
    void addFavorite_shouldThrowWhenAlreadyFavorited() {
        Long dishId = 1L;
        Long userId = 2L;
        when(dishService.getActiveById(dishId)).thenReturn(new Dish());
        doReturn(1L).when(favoriteService).count(any());

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> favoriteService.addFavorite(dishId, userId));

        assertEquals("已收藏", exception.getMessage());
        verify(favoriteService, never()).save(any(Favorite.class));
    }

    @Test
    void addFavorite_shouldSaveFavoriteWhenValid() {
        Long dishId = 1L;
        Long userId = 2L;
        when(dishService.getActiveById(dishId)).thenReturn(new Dish());
        doReturn(0L).when(favoriteService).count(any());
        doReturn(true).when(favoriteService).save(any(Favorite.class));

        favoriteService.addFavorite(dishId, userId);

        ArgumentCaptor<Favorite> favoriteCaptor = ArgumentCaptor.forClass(Favorite.class);
        verify(favoriteService).save(favoriteCaptor.capture());
        Favorite savedFavorite = favoriteCaptor.getValue();
        assertEquals(dishId, savedFavorite.getDishId());
        assertEquals(userId, savedFavorite.getUserId());
        assertNotNull(savedFavorite.getCreatedTime());
    }

    @Test
    void removeFavorite_shouldCallRemove() {
        Long dishId = 1L;
        Long userId = 2L;
        doReturn(true).when(favoriteService).remove(any());

        favoriteService.removeFavorite(dishId, userId);

        verify(favoriteService).remove(any());
    }
}
