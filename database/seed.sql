-- ============================================================================
-- Riya Industries - Demonstration Seed Data
-- NOTE: All items below are clearly labeled [DEMO] / placeholder entries
-- so the business owner can replace them via the Admin Dashboard.
-- ============================================================================

USE riya_industries;

-- Roles
INSERT IGNORE INTO roles (name, description) VALUES
('ROLE_ADMIN', 'Full workshop & website administration access');

-- Service Categories (5 core categories matching homepage strip)
INSERT IGNORE INTO service_categories (name, slug, description, image_url, display_order, active) VALUES
('Lathe Machining', 'lathe-machining', 'Conventional and precision lathe turning operations for industrial shafts, bushings, and sleeves.', '/images/demo/cat-lathe.jpg', 1, TRUE),
('Custom Job Work', 'custom-job-work', 'Customer-specific job-work manufacturing based on sample components or engineering drawings.', '/images/demo/cat-custom.jpg', 2, TRUE),
('Precision Turning', 'precision-turning', 'Accurate OD/ID turning, facing, threading, and taper operations for tight industrial tolerances.', '/images/demo/cat-turning.jpg', 3, TRUE),
('Component Machining', 'component-machining', 'Batch and single-piece machining of pins, flanges, couplings, rollers, and spacers.', '/images/demo/cat-components.jpg', 4, TRUE),
('Industrial Manufacturing', 'industrial-manufacturing', 'Heavy-duty metalworking and sub-assembly component fabrication for manufacturing plants.', '/images/demo/cat-industrial.jpg', 5, TRUE);

-- Services (Demo entries — editable in Admin Dashboard)
INSERT IGNORE INTO services (category_id, name, slug, short_description, full_description, capabilities, specifications, primary_image_url, featured, published, display_order)
SELECT id, 'Heavy & Precision Lathe Turning', 'heavy-precision-lathe-turning',
       '[DEMO] Accurate cylindrical turning, facing, and boring for industrial shafts and sleeves.',
       '[DEMO] Complete lathe turning job-work for mild steel, alloy steel, stainless steel, brass, and cast iron components.',
       'External turning, internal boring, step turning, grooving, parting',
       'Update with confirmed workshop chuck size and bed length in Admin Dashboard',
       '/images/demo/service-lathe.jpg', TRUE, TRUE, 1
FROM service_categories WHERE slug = 'lathe-machining';

INSERT IGNORE INTO services (category_id, name, slug, short_description, full_description, capabilities, specifications, primary_image_url, featured, published, display_order)
SELECT id, 'Custom Drawing-Based Job Work', 'custom-drawing-based-job-work',
       '[DEMO] Custom component machining from customer CAD drawings, sketches, or physical samples.',
       '[DEMO] Flexible single-piece prototypes and small-to-medium batch production tailored to customer specifications.',
       'Drawing interpretation, sample reverse-machining, multi-stage turning',
       'Supports metric and imperial tolerances as specified by customer drawing',
       '/images/demo/service-custom.jpg', TRUE, TRUE, 2
FROM service_categories WHERE slug = 'custom-job-work';

INSERT IGNORE INTO services (category_id, name, slug, short_description, full_description, capabilities, specifications, primary_image_url, featured, published, display_order)
SELECT id, 'Threading & Taper Machining', 'threading-taper-machining',
       '[DEMO] Precision internal and external threading, acme threads, and taper turning.',
       '[DEMO] Accurate thread cutting and taper profiles for industrial fasteners, spindles, and couplings.',
       'Metric & BSP/BSW threading, internal/external tapers, knurling',
       'Update with confirmed pitch & taper range in Admin Dashboard',
       '/images/demo/service-threading.jpg', TRUE, TRUE, 3
FROM service_categories WHERE slug = 'precision-turning';

INSERT IGNORE INTO services (category_id, name, slug, short_description, full_description, capabilities, specifications, primary_image_url, featured, published, display_order)
SELECT id, 'Bushings, Pins & Flanges', 'bushings-pins-flanges',
       '[DEMO] Custom machined industrial bushings, dowel pins, collars, and mounting flanges.',
       '[DEMO] Durable wear components machined for machinery maintenance and OEM assembly lines.',
       'Concentric boring, chamfering, face finishing, batch repeatability',
       'Materials: EN8, EN19, SS304, SS316, Brass, Bronze, Mild Steel',
       '/images/demo/service-components.jpg', TRUE, TRUE, 4
FROM service_categories WHERE slug = 'component-machining';

-- Machinery (Demo placeholders — owner replaces with actual machine photos & specs)
INSERT IGNORE INTO machines (name, slug, machine_type, model, specifications, capabilities, additional_details, primary_image_url, active, display_order) VALUES
('[DEMO] Heavy-Duty Center Lathe', 'demo-heavy-duty-center-lathe', 'Center Lathe Machine', 'Update Model in Admin',
 'Update confirmed swing over bed, center distance, and spindle bore in Admin Dashboard.',
 'Heavy turning, facing, boring, and long shaft machining',
 'Placeholder demonstration entry — replace with actual workshop machine photo and specifications.',
 '/images/demo/machine-lathe-1.jpg', TRUE, 1),
('[DEMO] Precision Toolroom Lathe', 'demo-precision-toolroom-lathe', 'Precision Lathe', 'Update Model in Admin',
 'Update confirmed spindle speeds, chuck capacity, and bed length in Admin Dashboard.',
 'Fine finishing, threading, bushing and pin machining',
 'Placeholder demonstration entry — replace with actual workshop machine photo and specifications.',
 '/images/demo/machine-lathe-2.jpg', TRUE, 2);

-- Completed Work / Projects (Demo placeholders)
INSERT IGNORE INTO projects (service_id, title, slug, description, machining_type, material, industry, primary_image_url, featured, published, display_order)
SELECT id, '[DEMO] Machined Industrial Drive Shaft', 'demo-machined-drive-shaft',
       '[DEMO] Stepped shaft turned and finished to drawing requirements.',
       'Lathe Turning', 'Alloy Steel (Demo)', 'General Manufacturing',
       '/images/demo/project-shaft.jpg', TRUE, TRUE, 1
FROM services WHERE slug = 'heavy-precision-lathe-turning';

INSERT IGNORE INTO projects (service_id, title, slug, description, machining_type, material, industry, primary_image_url, featured, published, display_order)
SELECT id, '[DEMO] Precision Bronze & Steel Bushings', 'demo-precision-bushings',
       '[DEMO] Concentric bored bushings for industrial machinery maintenance.',
       'Boring & Turning', 'Bronze / Mild Steel (Demo)', 'Industrial Equipment',
       '/images/demo/project-bushing.jpg', TRUE, TRUE, 2
FROM services WHERE slug = 'bushings-pins-flanges';

INSERT IGNORE INTO projects (service_id, title, slug, description, machining_type, material, industry, primary_image_url, featured, published, display_order)
SELECT id, '[DEMO] Custom Threaded Spindle Collar', 'demo-threaded-spindle-collar',
       '[DEMO] Precision internal threading and face finishing job work.',
       'Threading', 'EN19 Steel (Demo)', 'Machinery & Tooling',
       '/images/demo/project-collar.jpg', TRUE, TRUE, 3
FROM services WHERE slug = 'threading-taper-machining';

-- Website Settings (Business info — update via Admin -> Website Settings)
INSERT IGNORE INTO website_settings (setting_key, setting_value, setting_group, description) VALUES
('business_name', 'Riya Industries', 'BUSINESS', 'Official business name displayed across site'),
('business_tagline', 'Precision Lathe Job-Work & Industrial Machining', 'BUSINESS', 'Short tagline'),
('contact_phone', '+91-00000-00000', 'CONTACT', 'Primary phone number for click-to-call (update in Admin)'),
('whatsapp_number', '910000000000', 'CONTACT', 'WhatsApp number with country code, no + or spaces'),
('contact_email', 'contact@riyaindustries.example', 'CONTACT', 'Primary enquiry email address (update in Admin)'),
('workshop_address', 'Update your workshop address in Admin -> Website Settings', 'CONTACT', 'Physical workshop address'),
('working_hours', 'Mon - Sat: 9:00 AM - 7:00 PM', 'CONTACT', 'Workshop operating hours'),
('google_maps_embed_url', '', 'CONTACT', 'Optional Google Maps embed iframe URL'),
('ecommerce_enabled', 'false', 'FEATURES', 'Toggle future own-brand product catalogue visibility (true/false)');

-- Future Product Category (Inactive / ready for expansion)
INSERT IGNORE INTO product_categories (name, slug, description, display_order, active) VALUES
('Industrial Machined Components', 'industrial-machined-components', 'Future own-brand manufactured product line.', 1, TRUE);
