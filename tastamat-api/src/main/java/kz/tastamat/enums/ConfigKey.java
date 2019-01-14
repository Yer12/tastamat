package kz.tastamat.enums;

public enum ConfigKey {
	MQTT, HOST, PORT, USERNAME, PASSWORD, SYNC_TOPIC, ASYNC_TOPIC,
	SMS, LOGIN, SENDER_ID, MSG_TYPE, USER_MSG_ID, SCHEDULED, PRIORITY, CORE, POST, URL, KEY, TOKEN, ENABLED,
	AUTH, TYPE, PATH, BILLING, PRICE, PROFILE, WALLET, TEMPLATE, PREFIX;

	public String key() {
		return this.name().toLowerCase();
	}
}
