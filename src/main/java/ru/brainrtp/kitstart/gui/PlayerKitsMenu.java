package ru.brainrtp.kitstart.gui;

import com.google.common.reflect.TypeToken;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.MMOItem;
import net.Indyuce.mmoitems.manager.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import ru.brainrtp.kitstart.KitStart;
import ru.brainrtp.kitstart.utils.Kit;
import ru.brainrtp.kitstart.utils.XMaterial;
import ru.brainrtp.kitstart.yml.LanguageConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PlayerKitsMenu implements Listener {

	private static LanguageConfig lang;
	public static List<String> players = new ArrayList<>();
	public static Menu menu;


	public static void defineStaticItems(LanguageConfig lang) {
		PlayerKitsMenu.lang = lang;
	}

	public static void open(Player player) {

//		Bukkit.getLogger().info(ChatColor.RED + "[DEBUG] [KitStart] > " + ChatColor.RESET + "player.openInventory(menu.get<...>);");

//		Bukkit.getLogger().info(ChatColor.RED + "[DEBUG] [KitStart] > " + ChatColor.RESET + "menu.getInventory.getContents() = " + Arrays.toString(menu.getInventory().getContents()));

		player.openInventory(menu.getInventory());
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory().getHolder() instanceof Menu) {
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

			ItemStack item = event.getCurrentItem();
			if(item == null || item.getType().equals(Material.AIR))
				return;

			else if (event.getSlot() == 22) {
				KitStart.usersList.add(player.getName());
				player.kickPlayer(lang.getMsg("kickMessage", false));
				final UUID uuid = player.getUniqueId();
				final Path playerPath = Bukkit.getServer().getWorldContainer().toPath()
						.resolve(Bukkit.getWorlds().get(0).getName())
						.resolve("playerdata")
						.resolve(uuid + ".dat");
				try {
					Files.delete(playerPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			for(Kit kit : KitStart.kits.values()) {
				if (kit.getName().equals(item.getItemMeta().getDisplayName())) {
					for (Map.Entry<String, List> entry : kit.getEquipments().entrySet()) {
						entry.getKey();
						entry.getValue();
						String name = null;
						if (entry.getKey().equals("items")) {
							entry.getValue().forEach(itemKit -> {
								String newItem = (String) itemKit;
								ItemStack is = null;
								int ammount;
								if (newItem.split(":")[0].equals("MMOITEMS")) {
									String type = newItem.split(":")[1];
									String name1 = newItem.split(":")[2];
									ammount = Integer.parseInt(newItem.split(":")[3]);
//									is = MMOItems.getItem(Type.valueOf(type), name1);

									ItemManager itemManager = MMOItems.getItems();
									MMOItem mmoitem = itemManager.getMMOItem(MMOItems.getTypes().get(type), name1);
									is = mmoitem.newBuilder().build();

									List<String> lore_m = is.getItemMeta().getLore();
									Material mat_m = is.getType();
									String name_m = is.getItemMeta().getDisplayName();
									is.setAmount(ammount);
								} else {
									if (newItem.split(":")[0].equals("TIPPED_ARROW")) {
										is = new ItemStack(Material.TIPPED_ARROW);
										PotionMeta potionMeta = (PotionMeta) is.getItemMeta();
//										potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 0), true);
										potionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
										is.setItemMeta(potionMeta);
										ammount = Integer.parseInt(newItem.split(":")[1]);
										is.setAmount(ammount);

									} else {
										is = XMaterial.valueOf(newItem.split(":")[0]).parseItem();
										ammount = Integer.parseInt(newItem.split(":")[1]);
										is.setAmount(ammount);
									}
								}
								player.getInventory().addItem(is);
							});
						}
						if (entry.getKey().equals("helmet")) {
							name = String.valueOf(entry.getValue().get(0)).split(":")[2];
//							ItemStack is = MMOItems.getItem(Type.valueOf("ARMOR"), name);
							ItemManager itemManager = MMOItems.getItems();
							MMOItem mmoitem = itemManager.getMMOItem(MMOItems.getTypes().get("ARMOR"), name);
							ItemStack is = mmoitem.newBuilder().build();
							player.getInventory().setHelmet(is);
						}

						if (entry.getKey().equals("chestplate")) {
							name = String.valueOf(entry.getValue().get(0)).split(":")[2];
//							ItemStack is = MMOItems.getItem(Type.valueOf("ARMOR"), name);

//							ItemStack is = MMOItems.getItems().getItem(MMOItems.getTypes().get("ARMOR"), name);
							ItemManager itemManager = MMOItems.getItems();
							MMOItem mmoitem = itemManager.getMMOItem(MMOItems.getTypes().get("ARMOR"), name);
							ItemStack is = mmoitem.newBuilder().build();
							player.getInventory().setChestplate(is);
						}

						if (entry.getKey().equals("leggings")) {
							name = String.valueOf(entry.getValue().get(0)).split(":")[2];
//							ItemStack is = MMOItems.getItem(Type.valueOf("ARMOR"), name);
							ItemManager itemManager = MMOItems.getItems();
							MMOItem mmoitem = itemManager.getMMOItem(MMOItems.getTypes().get("ARMOR"), name);
							ItemStack is = mmoitem.newBuilder().build();
							player.getInventory().setLeggings(is);
						}

						if (entry.getKey().equals("boots")) {
							name = String.valueOf(entry.getValue().get(0)).split(":")[2];
//							ItemStack is = MMOItems.getItem(Type.valueOf("ARMOR"), name);
							ItemManager itemManager = MMOItems.getItems();
							MMOItem mmoitem = itemManager.getMMOItem(MMOItems.getTypes().get("ARMOR"), name);
							ItemStack is = mmoitem.newBuilder().build();
							player.getInventory().setBoots(is);
						}
					}
					players.remove(player.getName());
					KitStart.usersList.remove(player.getName());
					player.closeInventory();
					player.sendMessage(lang.getMsg("kitSelected", true).replace("%s", kit.getName()));

					User user = KitStart.getLuckPermsAPI().getUserManager().getUser(player.getUniqueId());
					Node node = KitStart.getLuckPermsAPI().buildNode(kit.getPermission()).setValue(true).build();
					user.setPermission(node);
					KitStart.getLuckPermsAPI().getUserManager().saveUser(user);

					return;
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if(event.getInventory().getHolder() instanceof Menu) {
			if(event.getPlayer() instanceof Player) {
				if (((Player) event.getPlayer()).isOnline()) {
					if (players.contains(event.getPlayer().getName())) {
						Bukkit.getServer().getScheduler().runTask(KitStart.getPlugin(), () -> event.getPlayer().openInventory(menu.getInventory()));
//					System.out.println("Восстановлено");
					}
				}
			}
		}
	}

}
