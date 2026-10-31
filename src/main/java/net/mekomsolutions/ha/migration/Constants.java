package net.mekomsolutions.ha.migration;

public class Constants {
	
	public final static String ARG_ENTITY_TYPE = "type";
	
	public final static String ARG_FILE = "file";
	
	public final static String PROP_DATA_FILE_DIR = "data.file.directory";
	
	public final static String PROP_TIME_ZONE = "data.time.zone";
	
	public final static String PROP_BATCH_SIZE = "batch.size";
	
	public final static String PROP_SYNC_SOURCE_ID = "sync.source.id";
	
	public final static String PROP_SYNC_QUEUE_NAME = "sync.queue.name";
	
	public final static String PROP_ID_TYPE_UUID_ID = "id.type.uuid.id";
	
	public final static String PROP_ID_TYPE_UUID_GOVT = "id.type.uuid.govt";
	
	public final static String PROP_ATTR_TYPE_UUID_ETHNICITY = "attribute.type.uuid.ethnicity";
	
	public final static String PROP_ATTR_TYPE_UUID_MARITAL_STATUS = "attribute.type.uuid.marital.status";
	
	public final static String PROP_ATTR_TYPE_UUID_PHONE = "attribute.type.uuid.phone";
	
	public final static String PROP_ATTR_TYPE_UUID_BIRTH_PLACE = "attribute.type.uuid.birth.place";
	
	public final static String PROP_ATTR_TYPE_UUID_SPOUSE = "attribute.type.uuid.spouse";
	
	public final static String PROP_ATTR_TYPE_UUID_FATHER = "attribute.type.uuid.father";
	
	public final static String PROP_ATTR_TYPE_UUID_MOTHER = "attribute.type.uuid.mother";
	
	public final static String PROP_ATTR_TYPE_UUID_IGSS = "attribute.type.uuid.igss";
	
	public final static String PROP_ATTR_TYPE_UUID_EMERGENCY_CONTACT = "attribute.type.uuid.emergency.contact";
	
	public final static String PROP_ATTR_TYPE_UUID_EMERGENCY_PHONE = "attribute.type.uuid.emergency.phone";
	
	public final static String PROP_ATTR_TYPE_UUID_OCCUPATION = "attribute.type.uuid.occupation";
	
	public static final String DAEMON_USER_UUID = "A4F30A1B-5EB9-11DF-A648-37A07F9C90FB";
	
	public final static String OFFSET_FILE_SUFFIX = "-offset.json";
	
	public final static String DB_SYNC_VERSION = "4.0.0";
	
	public final static String DB_SYNC_OPERATION = "r";
	
	public final static String SYNC_PKG = "org.openmrs.eip.dbsync.";
	
	public final static String SYNC_CLASS_PATIENT = SYNC_PKG + "model.PatientModel";
	
	public final static String SYNC_CLASS_ID = SYNC_PKG + "model.PatientIdentifierModel";
	
	public final static String SYNC_CLASS_ADDRESS = SYNC_PKG + "model.PersonAddress";
	
	public final static String SYNC_CLASS_ATTRIBUTE = SYNC_PKG + "model.PersonAttribute";
	
	public final static String SYNC_CLASS_NAME = SYNC_PKG + "model.PersonName";
	
	public final static String SYNC_CLASS_PATIENT_LIGHT = SYNC_PKG + "entity.light.PatientLight";
	
	public final static String SYNC_CLASS_ID_LIGHT = SYNC_PKG + "entity.light.PatientIdentifierTypeLight";
	
	public final static String SYNC_CLASS_ATTRIB_TYPE_LIGHT = SYNC_PKG + "entity.light.PersonAttributeTypeLight";
	
	public final static String SYNC_CLASS_USER_LIGHT = SYNC_PKG + "entity.light.UserLight";
	
}
