package com.totaldowner.minefall;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.Chunk;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.totaldowner.minefall.living.MFMob;
import com.totaldowner.minefall.living.MFPlayer;

/**
 * Class that handles saving and loading of metadatas
 * 
 * @author Timothy Swartz
 * 
 */
public class MFPersistence {

    /**
     * Saves a players Mine Quest RPG data to an xml file
     * 
     * @param player
     *            the player to save
     * @param plugin
     *            the plugin object
     */
    public static void savePlayer(Player player, Plugin plugin) {
        String directoryName = plugin.getDataFolder().getPath() + File.separator + "players";
        String fileName = directoryName + File.separator + player.getName() + ".xml";

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("MFRPG");
            doc.appendChild(rootElement);

            Element pElement = doc.createElement("player");
            rootElement.appendChild(pElement);

            // Get metadata for player
            MFPlayer pdata = new MFPlayer(player, plugin);

            // Store single item player data
            pElement.setAttribute("name", pdata.getName());
            pElement.setAttribute("health", String.valueOf(pdata.getHealth()));
            // Stats
            pElement.setAttribute("strength", String.valueOf(pdata.getStrength()));
            pElement.setAttribute("stamina", String.valueOf(pdata.getStamina()));
            pElement.setAttribute("dexterity", String.valueOf(pdata.getDexterity()));
            pElement.setAttribute("intelligence", String.valueOf(pdata.getIntelligence()));
            pElement.setAttribute("wisdom", String.valueOf(pdata.getWisdom()));
            // Misc
            pElement.setAttribute("combatskill", pdata.getCombatSkill());
            pElement.setAttribute("damage", String.valueOf(pdata.getDamage()));
            // Skills
            pElement.setAttribute("unarmed", String.valueOf(pdata.getSkill("unarmed")));
            pElement.setAttribute("sword", String.valueOf(pdata.getSkill("sword")));
            pElement.setAttribute("archery", String.valueOf(pdata.getSkill("archery")));
            pElement.setAttribute("pickaxe", String.valueOf(pdata.getSkill("pickaxe")));
            pElement.setAttribute("axe", String.valueOf(pdata.getSkill("axe")));
            pElement.setAttribute("shovel", String.valueOf(pdata.getSkill("shovel")));
            pElement.setAttribute("hoe", String.valueOf(pdata.getSkill("hoe")));
            pElement.setAttribute("heavyarmor", String.valueOf(pdata.getSkill("heavyarmor")));
            pElement.setAttribute("lightarmor", String.valueOf(pdata.getSkill("lightarmor")));
            pElement.setAttribute("mining", String.valueOf(pdata.getSkill("mining")));
            pElement.setAttribute("woodcutting", String.valueOf(pdata.getSkill("woodcutting")));
            pElement.setAttribute("digging", String.valueOf(pdata.getSkill("digging")));
            pElement.setAttribute("farming", String.valueOf(pdata.getSkill("farming")));

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));

            // make the directories if they dont exist
            File theDir = new File(directoryName);
            if (!theDir.exists())
                theDir.mkdirs();

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            plugin.getLogger().info("ParserConfigurationException");
        } catch (TransformerException tfe) {
            plugin.getLogger().info("TransformerException " + tfe.getMessage());
        } catch (Exception e) {
            plugin.getLogger().info("Exception: " + e.getMessage());
        }
    }

    /**
     * Loads a players data from an xml file
     * 
     * @param player
     *            the player to load
     * @param plugin
     *            the plugin object
     */
    public static void loadPlayer(Player player, Plugin plugin) {
        String directoryName = plugin.getDataFolder().getPath() + File.separator + "players";
        String fileName = directoryName + File.separator + player.getName() + ".xml";

        try {
            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            Element playerNode = (Element) doc.getElementsByTagName("player").item(0);

            MFPlayer mPlayer = new MFPlayer(player, plugin);

            // Get player single infos
            NamedNodeMap attr = playerNode.getAttributes();
            if (!playerNode.getAttribute("name").isEmpty())
                mPlayer.setName(attr.getNamedItem("name").getTextContent());
            if (!playerNode.getAttribute("health").isEmpty())
                mPlayer.setHealth(Double.valueOf(attr.getNamedItem("health").getTextContent()));
            // Stats
            if (!playerNode.getAttribute("strength").isEmpty())
                mPlayer.setStrength(Double.valueOf(attr.getNamedItem("strength").getTextContent()));
            if (!playerNode.getAttribute("stamina").isEmpty())
                mPlayer.setStamina(Double.valueOf(attr.getNamedItem("stamina").getTextContent()));
            if (!playerNode.getAttribute("dexterity").isEmpty())
                mPlayer.setDexterity(Double.valueOf(attr.getNamedItem("dexterity").getTextContent()));
            if (!playerNode.getAttribute("intelligence").isEmpty())
                mPlayer.setIntelligence(Double.valueOf(attr.getNamedItem("intelligence").getTextContent()));
            if (!playerNode.getAttribute("wisdom").isEmpty())
                mPlayer.setWisdom(Double.valueOf(attr.getNamedItem("wisdom").getTextContent()));
            // Misc
            if (!playerNode.getAttribute("combatskill").isEmpty())
                mPlayer.setCombatSkill(attr.getNamedItem("combatskill").getTextContent());
            if (!playerNode.getAttribute("damage").isEmpty())
                mPlayer.setDamage(Double.valueOf(attr.getNamedItem("damage").getTextContent()));
            // skills
            if (!playerNode.getAttribute("unarmed").isEmpty())
                mPlayer.setSkill("unarmed", Double.valueOf(attr.getNamedItem("unarmed").getTextContent()));
            if (!playerNode.getAttribute("sword").isEmpty())
                mPlayer.setSkill("sword", Double.valueOf(attr.getNamedItem("sword").getTextContent()));
            if (!playerNode.getAttribute("archery").isEmpty())
                mPlayer.setSkill("archery", Double.valueOf(attr.getNamedItem("archery").getTextContent()));
            if (!playerNode.getAttribute("pickaxe").isEmpty())
                mPlayer.setSkill("pickaxe", Double.valueOf(attr.getNamedItem("pickaxe").getTextContent()));
            if (!playerNode.getAttribute("axe").isEmpty())
                mPlayer.setSkill("axe", Double.valueOf(attr.getNamedItem("axe").getTextContent()));
            if (!playerNode.getAttribute("shovel").isEmpty())
                mPlayer.setSkill("shovel", Double.valueOf(attr.getNamedItem("shovel").getTextContent()));
            if (!playerNode.getAttribute("hoe").isEmpty())
                mPlayer.setSkill("hoe", Double.valueOf(attr.getNamedItem("hoe").getTextContent()));
            if (!playerNode.getAttribute("heavyarmor").isEmpty())
                mPlayer.setSkill("heavyarmor", Double.valueOf(attr.getNamedItem("heavyarmor").getTextContent()));
            if (!playerNode.getAttribute("lightarmor").isEmpty())
                mPlayer.setSkill("lightarmor", Double.valueOf(attr.getNamedItem("lightarmor").getTextContent()));
            if (!playerNode.getAttribute("mining").isEmpty())
                mPlayer.setSkill("mining", Double.valueOf(attr.getNamedItem("mining").getTextContent()));
            if (!playerNode.getAttribute("woodcutting").isEmpty())
                mPlayer.setSkill("woodcutting", Double.valueOf(attr.getNamedItem("woodcutting").getTextContent()));
            if (!playerNode.getAttribute("digging").isEmpty())
                mPlayer.setSkill("digging", Double.valueOf(attr.getNamedItem("digging").getTextContent()));
            if (!playerNode.getAttribute("farming").isEmpty())
                mPlayer.setSkill("farming", Double.valueOf(attr.getNamedItem("farming").getTextContent()));

        } catch (FileNotFoundException e) {

        } catch (Exception e) {
            plugin.getLogger().info("Failed to load Player persistence: " + e.getMessage());
        }

    }

    /**
     * Saves all data for things associated with a chunk.
     * 
     * Needed to save mobs in a specific chunk for persistence.
     * 
     * @param chunk
     *            the chunk to save data for
     * @param plugin
     *            the plugin object
     */
    public static void saveChunk(Chunk chunk, Plugin plugin) {

        String directoryName = plugin.getDataFolder().getPath() + File.separator + "mob_chunks" + File.separator + chunk.getWorld().getName();
        String fileName = directoryName + File.separator + String.valueOf(chunk.getX() / 20) + "_" + String.valueOf(chunk.getZ() / 20);

        // Save mobs
        try {

            File xmlFile = new File(fileName);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc;
            Element rootElement;

            if (!xmlFile.exists()) {
                doc = docBuilder.newDocument();
                rootElement = doc.createElement("MFRPG");
                doc.appendChild(rootElement);

            } else {
                doc = docBuilder.parse(fileName);
                rootElement = (Element) doc.getElementsByTagName("MFRPG").item(0);
            }

            NodeList chunkNodes = doc.getElementsByTagName("chunk");
            boolean foundChunk = false;

            for (int x = 0; x < chunkNodes.getLength() && !foundChunk; x++) {
                NamedNodeMap attr = chunkNodes.item(x).getAttributes();
                if (attr.getNamedItem("xcoord").getTextContent().compareTo(String.valueOf(chunk.getX())) == 0 && attr.getNamedItem("zcoord").getTextContent().compareTo(String.valueOf(chunk.getZ())) == 0) {
                    // chunk found in XML so kill that MOFO

                    rootElement.removeChild(chunkNodes.item(x));
                    foundChunk = true;
                }
            }

            // create the chunk data in XML
            Element chunkElement = doc.createElement("chunk");
            rootElement.appendChild(chunkElement);
            chunkElement.setAttribute("xcoord", String.valueOf(chunk.getX()));
            chunkElement.setAttribute("zcoord", String.valueOf(chunk.getZ()));

            // Add mobs
            Entity[] e = chunk.getEntities();
            for (int x = 0; x < e.length; x++) {

                if (e[x] instanceof Creature) {
                    MFMob m = new MFMob((Creature) e[x], plugin);
                    if (m.getMaxHealth() > 0) {
                        Element mob = doc.createElement("mob");
                        chunkElement.appendChild(mob);
                        mob.setAttribute("id", e[x].getUniqueId().toString());
                        mob.setAttribute("name", m.getName());
                        mob.setAttribute("mobtype", m.getMobType());
                        mob.setAttribute("health", String.valueOf(m.getHealth()));
                        mob.setAttribute("strength", String.valueOf(m.getStrength()));
                        mob.setAttribute("stamina", String.valueOf(m.getStamina()));
                        mob.setAttribute("dexterity", String.valueOf(m.getDexterity()));
                        mob.setAttribute("intelligence", String.valueOf(m.getIntelligence()));
                        mob.setAttribute("wisdom", String.valueOf(m.getWisdom()));
                        // Misc
                        mob.setAttribute("combatskill", m.getCombatSkill());
                        mob.setAttribute("damage", String.valueOf(m.getDamage()));
                        // Skills
                        mob.setAttribute("unarmed", String.valueOf(m.getSkill("unarmed")));
                        mob.setAttribute("sword", String.valueOf(m.getSkill("sword")));
                        mob.setAttribute("archery", String.valueOf(m.getSkill("archery")));
                        mob.setAttribute("pickaxe", String.valueOf(m.getSkill("pickaxe")));
                        mob.setAttribute("axe", String.valueOf(m.getSkill("axe")));
                        mob.setAttribute("shovel", String.valueOf(m.getSkill("shovel")));
                        mob.setAttribute("hoe", String.valueOf(m.getSkill("hoe")));
                        mob.setAttribute("heavyarmor", String.valueOf(m.getSkill("heavyarmor")));
                        mob.setAttribute("lightarmor", String.valueOf(m.getSkill("lightarmor")));
                        mob.setAttribute("mining", String.valueOf(m.getSkill("mining")));
                        mob.setAttribute("woodcutting", String.valueOf(m.getSkill("woodcutting")));
                        mob.setAttribute("digging", String.valueOf(m.getSkill("digging")));
                        mob.setAttribute("farming", String.valueOf(m.getSkill("farming")));
                    }
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));

            // make the directories if they dont exist
            File theDir = new File(directoryName);
            if (!theDir.exists())
                theDir.mkdirs();

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            plugin.getLogger().info("ParserConfigurationException");
        } catch (TransformerException tfe) {
            plugin.getLogger().info("TransformerException " + tfe.getMessage());
        } catch (SAXException e) {
            plugin.getLogger().info("SAXException " + e.getMessage());
        } catch (IOException e) {
            plugin.getLogger().info("IOException " + e.getMessage());
        } catch (Exception e) {
            plugin.getLogger().info("Exception: " + e.getMessage());
        }
    }

    /**
     * Loads data from a specific chunk.
     * 
     * @param chunk
     *            the chunk to load.
     * @param plugin
     *            the plugin object.
     */
    public static void loadChunk(Chunk chunk, Plugin plugin) {

        String directoryName = plugin.getDataFolder().getPath() + File.separator + "mob_chunks" + File.separator + chunk.getWorld().getName();
        String fileName = directoryName + File.separator + String.valueOf(chunk.getX() / 20) + "_" + String.valueOf(chunk.getZ() / 20);

        // load monster persistence
        try {

            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            NodeList chunkNodes = doc.getElementsByTagName("chunk");
            boolean foundChunk = false;

            for (int x = 0; x < chunkNodes.getLength() && !foundChunk; x++) {
                NamedNodeMap attr = chunkNodes.item(x).getAttributes();
                if (attr.getNamedItem("xcoord").getTextContent().compareTo(String.valueOf(chunk.getX())) == 0 && attr.getNamedItem("zcoord").getTextContent().compareTo(String.valueOf(chunk.getZ())) == 0) {
                    // chunk found in XML so load it

                    NodeList nList = chunkNodes.item(x).getChildNodes();

                    for (int temp = 0; temp < nList.getLength(); temp++) {

                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;

                            boolean found = false;

                            if (eElement.getTagName().compareTo("mob") != 0)
                                found = true;

                            Entity[] e = chunk.getEntities();
                            for (int y = 0; y < e.length && !found; y++) {

                                if (e[y].getUniqueId().toString().compareTo(eElement.getAttribute("id")) == 0) {
                                    MFMob m = new MFMob((Creature) e[y], plugin);

                                    if (!eElement.getAttribute("name").isEmpty())
                                        m.setName(eElement.getAttribute("name"));
                                    if (!eElement.getAttribute("mobtype").isEmpty())
                                        m.setMobType(eElement.getAttribute("mobtype"));
                                    if (!eElement.getAttribute("health").isEmpty())
                                        m.setHealth(Double.valueOf(eElement.getAttribute("health")));
                                    if (!eElement.getAttribute("strength").isEmpty())
                                        m.setStrength(Double.valueOf(eElement.getAttribute("strength")));
                                    if (!eElement.getAttribute("stamina").isEmpty())
                                        m.setStamina(Double.valueOf(eElement.getAttribute("stamina")));
                                    if (!eElement.getAttribute("dexterity").isEmpty())
                                        m.setDexterity(Double.valueOf(eElement.getAttribute("dexterity")));
                                    if (!eElement.getAttribute("intelligence").isEmpty())
                                        m.setIntelligence(Double.valueOf(eElement.getAttribute("intelligence")));
                                    if (!eElement.getAttribute("wisdom").isEmpty())
                                        m.setWisdom(Double.valueOf(eElement.getAttribute("wisdom")));

                                    if (!eElement.getAttribute("combatskill").isEmpty())
                                        m.setCombatSkill(eElement.getAttribute("combatskill"));
                                    if (!eElement.getAttribute("damage").isEmpty())
                                        m.setDamage(Double.valueOf(eElement.getAttribute("damage")));

                                    if (!eElement.getAttribute("unarmed").isEmpty())
                                        m.setSkill("unarmed", Double.valueOf(eElement.getAttribute("unarmed")));
                                    if (!eElement.getAttribute("sword").isEmpty())
                                        m.setSkill("sword", Double.valueOf(eElement.getAttribute("sword")));
                                    if (!eElement.getAttribute("archery").isEmpty())
                                        m.setSkill("archery", Double.valueOf(eElement.getAttribute("archery")));
                                    if (!eElement.getAttribute("pickaxe").isEmpty())
                                        m.setSkill("pickaxe", Double.valueOf(eElement.getAttribute("pickaxe")));
                                    if (!eElement.getAttribute("axe").isEmpty())
                                        m.setSkill("axe", Double.valueOf(eElement.getAttribute("axe")));
                                    if (!eElement.getAttribute("shovel").isEmpty())
                                        m.setSkill("shovel", Double.valueOf(eElement.getAttribute("shovel")));
                                    if (!eElement.getAttribute("hoe").isEmpty())
                                        m.setSkill("hoe", Double.valueOf(eElement.getAttribute("hoe")));
                                    if (!eElement.getAttribute("heavyarmor").isEmpty())
                                        m.setSkill("heavyarmor", Double.valueOf(eElement.getAttribute("heavyarmor")));
                                    if (!eElement.getAttribute("lightarmor").isEmpty())
                                        m.setSkill("lightarmor", Double.valueOf(eElement.getAttribute("lightarmor")));
                                    if (!eElement.getAttribute("mining").isEmpty())
                                        m.setSkill("mining", Double.valueOf(eElement.getAttribute("mining")));
                                    if (!eElement.getAttribute("woodcutting").isEmpty())
                                        m.setSkill("woodcutting", Double.valueOf(eElement.getAttribute("woodcutting")));
                                    if (!eElement.getAttribute("digging").isEmpty())
                                        m.setSkill("digging", Double.valueOf(eElement.getAttribute("digging")));
                                    if (!eElement.getAttribute("farming").isEmpty())
                                        m.setSkill("farming", Double.valueOf(eElement.getAttribute("farming")));

                                    found = true;
                                }
                            }

                            if (!found) {
                                plugin.getLogger().info("ID not loaded from persistence file:" + eElement.getAttribute("id"));
                            }
                        }
                    }

                    foundChunk = true;
                }
            }

        } catch (FileNotFoundException e) {

        } catch (Exception e) {
            plugin.getLogger().info("Failed to load Monster persistence: " + e.getMessage());
        }
    }
}
