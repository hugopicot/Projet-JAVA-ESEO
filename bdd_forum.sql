-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 04 fév. 2026 à 08:37
-- Version du serveur : 9.1.0
-- Version de PHP : 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `bdd_forum`
--

-- --------------------------------------------------------

--
-- Structure de la table `commentaire`
--

DROP TABLE IF EXISTS `commentaire`;
CREATE TABLE IF NOT EXISTS `commentaire` (
  `id_commentaire` int NOT NULL AUTO_INCREMENT,
  `contenu` text NOT NULL,
  `date_creation` datetime DEFAULT CURRENT_TIMESTAMP,
  `score` int DEFAULT '0',
  `id_utilisateur` int DEFAULT NULL,
  `id_post` int DEFAULT NULL,
  `id_parent` int DEFAULT NULL,
  PRIMARY KEY (`id_commentaire`),
  KEY `id_utilisateur` (`id_utilisateur`),
  KEY `id_post` (`id_post`),
  KEY `id_parent` (`id_parent`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

DROP TABLE IF EXISTS `message`;
CREATE TABLE IF NOT EXISTS `message` (
  `id_message` int NOT NULL AUTO_INCREMENT,
  `contenu` text,
  `date_envoi` datetime DEFAULT CURRENT_TIMESTAMP,
  `lu` tinyint(1) DEFAULT '0',
  `id_expediteur` int DEFAULT NULL,
  `id_destinataire` int DEFAULT NULL,
  PRIMARY KEY (`id_message`),
  KEY `id_expediteur` (`id_expediteur`),
  KEY `id_destinataire` (`id_destinataire`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `moderation`
--

DROP TABLE IF EXISTS `moderation`;
CREATE TABLE IF NOT EXISTS `moderation` (
  `id_moderation` int NOT NULL AUTO_INCREMENT,
  `id_utilisateur` int DEFAULT NULL,
  `id_subreddit` int DEFAULT NULL,
  PRIMARY KEY (`id_moderation`),
  KEY `id_utilisateur` (`id_utilisateur`),
  KEY `id_subreddit` (`id_subreddit`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `post`
--

DROP TABLE IF EXISTS `post`;
CREATE TABLE IF NOT EXISTS `post` (
  `id_post` int NOT NULL AUTO_INCREMENT,
  `titre` varchar(255) NOT NULL,
  `contenu` text,
  `date_creation` datetime DEFAULT CURRENT_TIMESTAMP,
  `score` int DEFAULT '0',
  `id_utilisateur` int DEFAULT NULL,
  `id_subreddit` int DEFAULT NULL,
  PRIMARY KEY (`id_post`),
  KEY `id_utilisateur` (`id_utilisateur`),
  KEY `id_subreddit` (`id_subreddit`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `signalement_commentaire`
--

DROP TABLE IF EXISTS `signalement_commentaire`;
CREATE TABLE IF NOT EXISTS `signalement_commentaire` (
  `id_signalement_com` int NOT NULL AUTO_INCREMENT,
  `id_utilisateur` int DEFAULT NULL,
  `id_commentaire` int DEFAULT NULL,
  PRIMARY KEY (`id_signalement_com`),
  KEY `id_utilisateur` (`id_utilisateur`),
  KEY `id_commentaire` (`id_commentaire`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `signalement_post`
--

DROP TABLE IF EXISTS `signalement_post`;
CREATE TABLE IF NOT EXISTS `signalement_post` (
  `id_signalement` int NOT NULL AUTO_INCREMENT,
  `id_utilisateur` int DEFAULT NULL,
  `id_post` int DEFAULT NULL,
  PRIMARY KEY (`id_signalement`),
  KEY `id_utilisateur` (`id_utilisateur`),
  KEY `id_post` (`id_post`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `subreddit`
--

DROP TABLE IF EXISTS `subreddit`;
CREATE TABLE IF NOT EXISTS `subreddit` (
  `id_subreddit` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `description` text,
  `date_creation` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_subreddit`),
  UNIQUE KEY `nom` (`nom`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id_utilisateur` int NOT NULL AUTO_INCREMENT,
  `pseudo` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `date_inscription` datetime DEFAULT CURRENT_TIMESTAMP,
  `karma` int DEFAULT '0',
  PRIMARY KEY (`id_utilisateur`),
  UNIQUE KEY `pseudo` (`pseudo`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `vote`
--

DROP TABLE IF EXISTS `vote`;
CREATE TABLE IF NOT EXISTS `vote` (
  `id_vote` int NOT NULL AUTO_INCREMENT,
  `type_vote` tinyint NOT NULL,
  `date_vote` datetime DEFAULT CURRENT_TIMESTAMP,
  `id_utilisateur` int DEFAULT NULL,
  `id_post` int DEFAULT NULL,
  `id_commentaire` int DEFAULT NULL,
  PRIMARY KEY (`id_vote`),
  KEY `id_utilisateur` (`id_utilisateur`),
  KEY `id_post` (`id_post`),
  KEY `id_commentaire` (`id_commentaire`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
