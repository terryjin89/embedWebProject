-- ============================================================
-- MySQL Character Set Configuration
-- ============================================================
-- This file must be executed FIRST (filename starts with 00-)
-- to ensure all subsequent operations use UTF-8 encoding
-- ============================================================

-- Set global character set variables
SET GLOBAL character_set_client = utf8mb4;
SET GLOBAL character_set_connection = utf8mb4;
SET GLOBAL character_set_results = utf8mb4;
SET GLOBAL character_set_database = utf8mb4;
SET GLOBAL collation_connection = utf8mb4_unicode_ci;
SET GLOBAL collation_database = utf8mb4_unicode_ci;

-- Set session character set variables
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET CHARACTER SET utf8mb4;

-- Verify settings (for debugging)
-- SHOW VARIABLES LIKE 'character%';
-- SHOW VARIABLES LIKE 'collation%';
