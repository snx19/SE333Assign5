package org.example.Amazon;

import org.example.Amazon.Cost.ExtraCostForElectronics;
import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AmazonUnitTest {

    private ShoppingCart mockCart;
    private PriceRule mockRule;
    private Amazon amazon;

    @BeforeEach
    public void setUp() {
        mockCart = mock(ShoppingCart.class);
        mockRule = mock(PriceRule.class);
        amazon = new Amazon(mockCart, List.of(mockRule));
    }

    @Test
    @DisplayName("specification-based: test when cart is empty")
    public void testEmptyCart() {
        when(mockCart.getItems()).thenReturn(List.of());
        when(mockRule.priceToAggregate(anyList())).thenReturn(0.0);

        // act
        double total = amazon.calculate();

        // assert
        assertThat(total).isEqualTo(0.0);
        verify(mockRule, times(1)).priceToAggregate(anyList());
    }

    @Test
    @DisplayName("specification-based: calculates total of items in cart")
    public void testCalculatesItems() {
        Item item1 = new Item(ItemType.ELECTRONIC, "Laptop", 1, 1000.0);
        Item item2 = new Item(ItemType.OTHER, "Notepad", 2, 5.0);

        when(mockCart.getItems()).thenReturn(List.of(item1, item2));
        when(mockRule.priceToAggregate(anyList())).thenReturn(1010.0);

        // act
        double total = amazon.calculate();

        // assert
        assertThat(total).isEqualTo(1010.0);
        verify(mockRule, times(1)).priceToAggregate(anyList());
    }

    @Test
    @DisplayName("specification-based: calculates extra cost of electronics")
    public void testCalculatesElectronicExtraCost() {
        Item item = new Item(ItemType.ELECTRONIC, "Laptop", 1, 1000.0);

        PriceRule electronicRule = new ExtraCostForElectronics();
        amazon = new Amazon(mockCart, List.of(electronicRule));

        when(mockCart.getItems()).thenReturn(List.of(item));

        // act
        double total = amazon.calculate();

        // assert
        assertThat(total).isEqualTo(7.50); // extra cost
    }

    @Test
    @DisplayName("structural-based: add items to cart")
    public void testAddToCart() {
        Item item = new Item(ItemType.OTHER, "Book", 2, 12.0);

        // act
        amazon.addToCart(item);

        // assert
        verify(mockCart).add(item);
    }
}
