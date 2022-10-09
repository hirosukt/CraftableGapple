package com.github.hirosukt

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val item = ItemStack(Material.ENCHANTED_GOLDEN_APPLE)

        val key = NamespacedKey(this, "enchanted_golden_apple")
        val recipe = ShapedRecipe(key, item)

        recipe.shape("GGG", "GAG", "GGG")

        recipe.setIngredient('G', Material.GOLD_BLOCK)
        recipe.setIngredient('A', Material.APPLE)

        Bukkit.addRecipe(recipe)
    }
}