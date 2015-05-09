#include <pebble.h>

static Window *window;

// Names of appliances
static TextLayer *heading_coffee_layer;
static TextLayer *heading_heating_layer;
static TextLayer *heading_lights_layer;
static TextLayer *heading_radio_layer;
static TextLayer *heading_television_layer;

// ON/OFF states of appliances
static TextLayer *coffee_layer;
static TextLayer *heating_layer;
static TextLayer *lights_layer;
static TextLayer *radio_layer;
static TextLayer *television_layer;

// Strings for names of apppliances
static char coffee[32];
static char heating[32];
static char lights[32];
static char radio[32];
static char television[32];

static AppSync sync;

static uint8_t NUMBER_OF_CLASSES = 5;

// Holds callbacks
static uint8_t sync_buffer[96];

enum ApplianceKey {
    APPLIANCE_COFFEE_KEY = 0x0,
    APPLIANCE_HEATING_KEY = 0x1,
    APPLIANCE_LIGHTS_KEY = 0x2,
    APPLIANCE_RADIO_KEY = 0x3,
    APPLIANCE_TELEVISION_KEY = 0x4,
    
};

static void sync_error_callback(DictionaryResult dict_error, AppMessageResult app_message_error, void *context) {
    APP_LOG(APP_LOG_LEVEL_DEBUG, "App Message Sync Error: %d", app_message_error);
}

// For when data is sent to the Pebble smartwatch
static void sync_tuple_changed_callback(const uint32_t key, const Tuple* new_tuple, const Tuple* old_tuple, void* context) {
    int8_t nState= new_tuple->value->int8;
    char *newState = "OFF";
    if (nState == 1){
        newState = "ON";
    }
    switch (key) {
        case APPLIANCE_COFFEE_KEY:
            text_layer_set_text(coffee_layer, newState);
            break;
        case APPLIANCE_HEATING_KEY:
            text_layer_set_text(heating_layer, newState);
            break;
        case APPLIANCE_LIGHTS_KEY:
            text_layer_set_text(lights_layer, newState);
            break;
        case APPLIANCE_RADIO_KEY:
            text_layer_set_text(radio_layer, newState);
            break;
        case APPLIANCE_TELEVISION_KEY:
            text_layer_set_text(television_layer, newState);
            break;
            
    }
}

// UI loading
static void window_load(Window *window) {
    Layer *window_layer = window_get_root_layer(window);
    
    // Setup Headings
    
    heading_coffee_layer = text_layer_create(GRect(10, 10, 72, 32));
    text_layer_set_text_color(heading_coffee_layer, GColorWhite);
    text_layer_set_background_color(heading_coffee_layer, GColorClear);
    text_layer_set_font(heading_coffee_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24_BOLD));
    text_layer_set_text_alignment(heading_coffee_layer, GTextAlignmentLeft);
    text_layer_set_text(heading_coffee_layer, "COFFEE:");
    
    heading_heating_layer = text_layer_create(GRect(10, 40, 72, 32));
    text_layer_set_text_color(heading_heating_layer, GColorWhite);
    text_layer_set_background_color(heading_heating_layer, GColorClear);
    text_layer_set_font(heading_heating_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24_BOLD));
    text_layer_set_text_alignment(heading_heating_layer, GTextAlignmentLeft);
    text_layer_set_text(heading_heating_layer, "HEATING:");
    
    heading_lights_layer = text_layer_create(GRect(10, 70, 72, 32));
    text_layer_set_text_color(heading_lights_layer, GColorWhite);
    text_layer_set_background_color(heading_lights_layer, GColorClear);
    text_layer_set_font(heading_lights_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24_BOLD));
    text_layer_set_text_alignment(heading_lights_layer, GTextAlignmentLeft);
    text_layer_set_text(heading_lights_layer, "LIGHTS:");
    
    heading_radio_layer = text_layer_create(GRect(10, 100, 72, 32));
    text_layer_set_text_color(heading_radio_layer, GColorWhite);
    text_layer_set_background_color(heading_radio_layer, GColorClear);
    text_layer_set_font(heading_radio_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24_BOLD));
    text_layer_set_text_alignment(heading_radio_layer, GTextAlignmentLeft);
    text_layer_set_text(heading_radio_layer, "RADIO:");
    
    heading_television_layer = text_layer_create(GRect(10, 130, 72, 32));
    text_layer_set_text_color(heading_television_layer, GColorWhite);
    text_layer_set_background_color(heading_television_layer, GColorClear);
    text_layer_set_font(heading_television_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24_BOLD));
    text_layer_set_text_alignment(heading_television_layer, GTextAlignmentLeft);
    text_layer_set_text(heading_television_layer, "TV:");
    
    // STATES (ON/OFF)
    coffee_layer = text_layer_create(GRect(100, 10, 48, 32));
    text_layer_set_text_color(coffee_layer, GColorWhite);
    text_layer_set_background_color(coffee_layer, GColorClear);
    text_layer_set_font(coffee_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24_BOLD));
    text_layer_set_text_alignment(coffee_layer, GTextAlignmentLeft);
    text_layer_set_text(coffee_layer, coffee);
    
    heating_layer = text_layer_create(GRect(100, 40, 48, 32));
    text_layer_set_text_color(heating_layer, GColorWhite);
    text_layer_set_background_color(heating_layer, GColorClear);
    text_layer_set_font(heating_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24_BOLD));
    text_layer_set_text_alignment(heating_layer, GTextAlignmentLeft);
    text_layer_set_text(heating_layer, heating);
    
    lights_layer = text_layer_create(GRect(100, 70, 48, 32));
    text_layer_set_text_color(lights_layer, GColorWhite);
    text_layer_set_background_color(lights_layer, GColorClear);
    text_layer_set_font(lights_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24_BOLD));
    text_layer_set_text_alignment(lights_layer, GTextAlignmentLeft);
    text_layer_set_text(lights_layer, lights);
    
    radio_layer = text_layer_create(GRect(100, 100, 48, 32));
    text_layer_set_text_color(radio_layer, GColorWhite);
    text_layer_set_background_color(radio_layer, GColorClear);
    text_layer_set_font(radio_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24_BOLD));
    text_layer_set_text_alignment(radio_layer, GTextAlignmentLeft);
    text_layer_set_text(radio_layer, radio);
    
    television_layer = text_layer_create(GRect(100, 130, 48, 32));
    text_layer_set_text_color(television_layer, GColorWhite);
    text_layer_set_background_color(television_layer, GColorClear);
    text_layer_set_font(television_layer, fonts_get_system_font(FONT_KEY_GOTHIC_24_BOLD));
    text_layer_set_text_alignment(television_layer, GTextAlignmentLeft);
    text_layer_set_text(television_layer, television);
    
    Tuplet initial_values[] = {
        TupletInteger(APPLIANCE_COFFEE_KEY, (int8_t) 0),
        TupletInteger(APPLIANCE_HEATING_KEY, (int8_t) 0),
        TupletInteger(APPLIANCE_LIGHTS_KEY, (int8_t) 0),
        TupletInteger(APPLIANCE_RADIO_KEY, (int8_t) 0),
        TupletInteger(APPLIANCE_TELEVISION_KEY, (int8_t) 0)
    };
    app_sync_init(&sync, sync_buffer, sizeof(sync_buffer), initial_values, ARRAY_LENGTH(initial_values),
                  sync_tuple_changed_callback, sync_error_callback, NULL);
    
    layer_add_child(window_layer, text_layer_get_layer(heading_coffee_layer));
    layer_add_child(window_layer, text_layer_get_layer(heading_heating_layer));
    layer_add_child(window_layer, text_layer_get_layer(heading_lights_layer));
    layer_add_child(window_layer, text_layer_get_layer(heading_radio_layer));
    layer_add_child(window_layer, text_layer_get_layer(heading_television_layer));
    layer_add_child(window_layer, text_layer_get_layer(coffee_layer));
    layer_add_child(window_layer, text_layer_get_layer(heating_layer));
    layer_add_child(window_layer, text_layer_get_layer(lights_layer));
    layer_add_child(window_layer, text_layer_get_layer(radio_layer));
    layer_add_child(window_layer, text_layer_get_layer(television_layer));
    
    // Refresh UI
    DictionaryIterator *iter;
    app_message_outbox_begin(&iter);
    Tuplet value = TupletInteger(1, (uint8_t) 1);
    dict_write_tuplet(iter, &value);
    app_message_outbox_send();
    
}

// Memory management for when the watchface
// disappears from view
static void window_unload(Window *window) {
    app_sync_deinit(&sync);
    
    text_layer_destroy(heading_coffee_layer);
    text_layer_destroy(heading_heating_layer);
    text_layer_destroy(heading_lights_layer);
    text_layer_destroy(heading_radio_layer);
    text_layer_destroy(heading_television_layer);
    
    text_layer_destroy(coffee_layer);
    text_layer_destroy(heating_layer);
    text_layer_destroy(lights_layer);
    text_layer_destroy(radio_layer);
    text_layer_destroy(television_layer);
}

void out_sent_handler(DictionaryIterator *sent, void *context) {
    // outgoing message was delivered
}


void out_failed_handler(DictionaryIterator *failed, AppMessageResult reason, void *context) {
    // outgoing message failed
}

// When the watchface comes into view
static void init() {
    window = window_create();
    window_set_background_color(window, GColorBlack);
    window_set_fullscreen(window, true);
    window_set_window_handlers(window, (WindowHandlers) {
        .load = window_load,
        .unload = window_unload
    });
    
    app_message_register_outbox_sent(out_sent_handler);
    app_message_register_outbox_failed(out_failed_handler);
    
    const int inbound_size = 64;
    const int outbound_size = 32;
    app_message_open(inbound_size, outbound_size);
    
    const bool animated = true;
    window_stack_push(window, animated);
}

// Additional memory management 
static void deinit() {
    window_destroy(window);
}

// Main loop of app
int main(void) {
    init();
    app_event_loop();
    deinit();
}
