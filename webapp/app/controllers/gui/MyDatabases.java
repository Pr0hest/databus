package controllers.gui;

import gov.nrel.util.Utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.DataTypeEnum;
import models.Entity;
import models.EntityGroup;
import models.EntityGroupXref;
import models.EntityUser;
import models.PermissionType;
import models.SecureResource;
import models.SecureResourceGroupXref;
import models.SecureSchema;
import models.SecureTable;

import org.apache.commons.lang.StringUtils;
import org.playorm.cron.api.CronService;
import org.playorm.cron.api.CronServiceFactory;
import org.playorm.cron.api.PlayOrmCronJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Controller;
import play.mvc.With;

import com.alvazan.orm.api.base.spi.UniqueKeyGenerator;
import com.alvazan.play.NoSql;

import controllers.TableMonitor;
import controllers.gui.auth.GuiSecure;

@With(GuiSecure.class)
public class MyDatabases extends Controller {
	
	static final Logger log = LoggerFactory.getLogger(MyDatabases.class);
	
	public static void myDatabases() {
		EntityUser user = Utility.getCurrentUser(session);

		List<SecureResourceGroupXref> resouceXrefs = user.getResources();
		if (log.isInfoEnabled())
			for (SecureResourceGroupXref resource : resouceXrefs) {
				log.info("resource=" + resource.getResource());
			}

		render(user);
	}

	public static void viewDatabaseImpl(String schemaName) {
		EntityUser user = Utility.getCurrentUser(session);

		SecureSchema schema = new SecureSchema();
		List<SecureTable> tables = new ArrayList<SecureTable>();
		if (schemaName != null) {
			schema = SecureSchema.findByName(NoSql.em(), schemaName);
			Set<PermissionType> roles = fetchRoles(schema, user);
			if(roles.contains(PermissionType.READ) || roles.contains(PermissionType.READ_WRITE)
					|| roles.contains(PermissionType.ADMIN)) {
				tables = schema.getTables(0, 50);
			} else if(roles.size() == 0) {
				notFound("Your user does not have access to this resource");
			} else
				viewDatabase(schemaName);
		}

		List<String> ids = schema.getMonitorIds();
		CronService svc = CronServiceFactory.getSingleton(null);
		List<PlayOrmCronJob> mons = svc.getMonitors(ids);
		List<TableMonitor> monitors = new ArrayList<TableMonitor>();
		for(PlayOrmCronJob m : mons) {
			monitors.add(TableMonitor.copy(m));
		}
		DataTypeEnum timeseries = DataTypeEnum.TIME_SERIES;
		render(user, schema, tables, monitors, timeseries);
	}
	
	public static void viewDatabase(String schemaName) {
		EntityUser user = Utility.getCurrentUser(session);

		SecureSchema schema = new SecureSchema();
		List<SecureTable> tables = new ArrayList<SecureTable>();
		if (schemaName != null) {
			schema = SecureSchema.findByName(NoSql.em(), schemaName);
			Set<PermissionType> roles = fetchRoles(schema, user);
			if(roles.contains(PermissionType.ADMIN)) {
				dbTables(schemaName);
			} else if(roles.size() == 0) {
				notFound("Your user does not have access to this resource");
			} else {
				tables = schema.getTables(0, 50);
			}
		}
		
		viewDatabaseImpl(schemaName);
	}
	
	public static void dbUsers(String schemaName) {
		EntityUser user = Utility.getCurrentUser(session);

		SecureSchema schema = new SecureSchema();
		schema.setCreator(user);
		List<SecureTable> tables = new ArrayList<SecureTable>();
		if (schemaName != null) {
			schema = SecureSchema.findByName(NoSql.em(), schemaName);
			Set<PermissionType> roles = fetchRoles(schema, user);
			if(roles.contains(PermissionType.ADMIN)) {
				tables = schema.getTables(0, 50);				
			} else if(roles.size() == 0) {
				notFound("Your user does not have access to this resource");
			} else
				viewDatabase(schemaName);
		}
		
		String oldSchemaName = schema.getSchemaName();
		List<String> ids = schema.getMonitorIds();
		CronService svc = CronServiceFactory.getSingleton(null);
		List<PlayOrmCronJob> mons = svc.getMonitors(ids);
		List<TableMonitor> monitors = new ArrayList<TableMonitor>();
		for(PlayOrmCronJob m : mons) {
			if(m != null)
				monitors.add(TableMonitor.copy(m));
		}
		DataTypeEnum timeseries = DataTypeEnum.TIME_SERIES;
		render(user, schema, oldSchemaName, tables, monitors, timeseries);
	}
	
	public static void dbCronJobs(String schemaName) {
		EntityUser user = Utility.getCurrentUser(session);

		SecureSchema schema = new SecureSchema();
		schema.setCreator(user);
		List<SecureTable> tables = new ArrayList<SecureTable>();
		if (schemaName != null) {
			schema = SecureSchema.findByName(NoSql.em(), schemaName);
			Set<PermissionType> roles = fetchRoles(schema, user);
			if(roles.contains(PermissionType.ADMIN)) {
				tables = schema.getTables(0, 50);				
			} else if(roles.size() == 0) {
				notFound("Your user does not have access to this resource");
			} else
				viewDatabase(schemaName);
		}
		
		String oldSchemaName = schema.getSchemaName();
		List<String> ids = schema.getMonitorIds();
		CronService svc = CronServiceFactory.getSingleton(null);
		List<PlayOrmCronJob> mons = svc.getMonitors(ids);
		List<TableMonitor> monitors = new ArrayList<TableMonitor>();
		for(PlayOrmCronJob m : mons) {
			if(m != null)
				monitors.add(TableMonitor.copy(m));
		}
		DataTypeEnum timeseries = DataTypeEnum.TIME_SERIES;
		render(user, schema, oldSchemaName, tables, monitors, timeseries);
	}
	
	public static void dbTables(String schemaName) {
		EntityUser user = Utility.getCurrentUser(session);

		SecureSchema schema = new SecureSchema();
		schema.setCreator(user);
		List<SecureTable> tables = new ArrayList<SecureTable>();
		if (schemaName != null) {
			schema = SecureSchema.findByName(NoSql.em(), schemaName);
			Set<PermissionType> roles = fetchRoles(schema, user);
			if(roles.contains(PermissionType.ADMIN)) {
				tables = schema.getTables(0, 50);				
			} else if(roles.size() == 0) {
				notFound("Your user does not have access to this resource");
			} else
				viewDatabase(schemaName);
		}
		
		String oldSchemaName = schema.getSchemaName();
		List<String> ids = schema.getMonitorIds();
		CronService svc = CronServiceFactory.getSingleton(null);
		List<PlayOrmCronJob> mons = svc.getMonitors(ids);
		List<TableMonitor> monitors = new ArrayList<TableMonitor>();
		for(PlayOrmCronJob m : mons) {
			if(m != null)
				monitors.add(TableMonitor.copy(m));
		}
		DataTypeEnum timeseries = DataTypeEnum.TIME_SERIES;
		render(user, schema, oldSchemaName, tables, monitors, timeseries);
	}

	public static void dbProperties(String schemaName) {
		EntityUser user = Utility.getCurrentUser(session);

		SecureSchema schema = new SecureSchema();
		schema.setCreator(user);
		List<SecureTable> tables = new ArrayList<SecureTable>();
		if (schemaName != null) {
			schema = SecureSchema.findByName(NoSql.em(), schemaName);
			Set<PermissionType> roles = fetchRoles(schema, user);
			if(roles.contains(PermissionType.ADMIN)) {
				tables = schema.getTables(0, 50);				
			} else if(roles.size() == 0) {
				notFound("Your user does not have access to this resource");
			} else
				viewDatabase(schemaName);
		}
		
		String oldSchemaName = schema.getSchemaName();
		List<String> ids = schema.getMonitorIds();
		CronService svc = CronServiceFactory.getSingleton(null);
		List<PlayOrmCronJob> mons = svc.getMonitors(ids);
		List<TableMonitor> monitors = new ArrayList<TableMonitor>();
		for(PlayOrmCronJob m : mons) {
			if(m != null)
				monitors.add(TableMonitor.copy(m));
		}
		DataTypeEnum timeseries = DataTypeEnum.TIME_SERIES;
		render(user, schema, oldSchemaName, tables, monitors, timeseries);
	}

	private static SecureSchema schemaCheck(String schema, EntityUser user, PermissionType permission) {
		SecureSchema theSchema = SecureSchema.findByName(NoSql.em(), schema);
		Set<PermissionType> roles = fetchRoles(theSchema, user);
		if(!roles.contains(permission))
			notFound("Your user does not have access to this resource");

		return theSchema;
	}

	private static Set<PermissionType> fetchRoles(SecureResource resource, EntityUser user) {
		if(resource == null)
			notFound("You user either does not have access or this schema does not exist");
		
		if (log.isInfoEnabled())
			log.info("resource found="+resource.getId()+" n2="+resource.getName());
		if(user.isAdmin()) {
			return PermissionType.allRoles();
		}

		Set<PermissionType> roles = new HashSet<PermissionType>();
		user.addResources(roles, resource);

		if(resource instanceof SecureTable) {
			if(log.isDebugEnabled())
				log.debug("resource is a table so adding roles for table");
			SecureTable t = (SecureTable) resource;
			user.addResources(roles, t.getSchema());
		}

		if(roles.size() == 0)
			notFound("Your user either does not have access or this schema does not exist");
		
		return roles;
	}
	
	private static void newDatabase(SecureSchema schema) {
		schema.setId(null); //playframework set this to "" for some reason(need to fix playframework there for Strings
		
		if (log.isInfoEnabled())
			log.info(" schema id=" + schema.getId() + " name=" + schema.getSchemaName());
		EntityUser user = Utility.getCurrentUser(session);

		// This is a new schema or name change, ensure name does not already
		// exist
		SecureSchema existing = SecureSchema.findByName(NoSql.em(),
				schema.getSchemaName());
		if (existing != null) {
			// If there is already a schema and it is not this schema(ie. ids are
			// different) we then have a problem...
			validation.addError("schema.schemaName",
					"This schema name is in use already");
		}

		if (validation.hasErrors()) {
			render("@editSchema", user, schema);
		}

		NoSql.em().fillInWithKey(schema);
		SecureResourceGroupXref xref = new SecureResourceGroupXref(user, schema, PermissionType.ADMIN);		
		NoSql.em().put(xref);
		
		NoSql.em().put(schema);
		NoSql.em().put(user);

		NoSql.em().flush();

		myDatabases();
	}
	
	private static void modifyOldDatabase(SecureSchema schema, String oldSchemaName) {

		if (log.isInfoEnabled())
			log.info(" schema id=" + schema.getId() + " name=" + schema.getSchemaName());
		EntityUser user = Utility.getCurrentUser(session);

		SecureSchema oldSchema = schemaCheck(oldSchemaName, user, PermissionType.ADMIN);

		if(!schema.getSchemaName().equals(oldSchema.getSchemaName())) {
			// This is a name change, ensure name does not already exist BUT can exist if id is same(ie. same schema)
			SecureSchema existing = SecureSchema.findByName(NoSql.em(), schema.getSchemaName());
			if (existing != null && !existing.getId().equals(schema.getId())) {
				// If there is already a group and it is not this group(ie. ids are
				// different) we then have a problem...
				validation.addError("schema.name",
						"This schema name is in use already");
			}
		}

		if (validation.hasErrors()) {
			render("@editSchema", user, schema);
		}

		NoSql.em().put(schema);
		NoSql.em().flush();
	}
	
	public static void tableAdd(String schema) {
		EntityUser user = Utility.getCurrentUser(session);
		SecureSchema actualSchema = schemaCheck(schema, user, PermissionType.ADMIN);
		
		SecureResource table = new SecureTable();
		render(actualSchema, table);
	}
	
	
	public static void postDatabase(SecureSchema schema, String oldSchemaName) {
		if(StringUtils.isEmpty(oldSchemaName)) {
			newDatabase(schema);
		} else 
			modifyOldDatabase(schema, oldSchemaName);

		//TODO:JSC reindex search data for altered/new schema here!
		myDatabases();
	}

	public static void monitorAdd(String schema) {
		EntityUser user = Utility.getCurrentUser(session);
		SecureSchema schemaDbo = schemaCheck(schema, user, PermissionType.ADMIN);
		render("@monitorEdit", schemaDbo);
	}
	
	public static void monitorEdit(String schema, String table) {
		EntityUser user = Utility.getCurrentUser(session);
		SecureSchema schemaDbo = schemaCheck(schema, user, PermissionType.ADMIN);
		
		String id = TableMonitor.formKey(schema, table);
		CronService svc = CronServiceFactory.getSingleton(null);
		PlayOrmCronJob mon = svc.getMonitor(id);
		if(mon == null)
			notFound();
		
		TableMonitor monitor = TableMonitor.copy(mon);
		
		render(schemaDbo, monitor);
	}
	
	public static void postMonitor(String schema, TableMonitor monitor) {
		EntityUser user = Utility.getCurrentUser(session);
		SecureSchema schemaDbo = schemaCheck(schema, user, PermissionType.ADMIN);
		
		SecureTable table = SecureTable.findByName(NoSql.em(), monitor.getTableName());
		if(table == null) {
			validation.addError("monitor.tableName", "This table does not exist");
		} else if(!table.getSchema().getName().equals(schema)) {
			validation.addError("monitor.tableName", "This table does not exist in this schema");
		}
		
		if(validation.hasErrors()) {
			render("@monitorEdit", schemaDbo, monitor);
			return;
		}
		
		PlayOrmCronJob playMonitor = TableMonitor.copy(schema, monitor);
		CronService svc = CronServiceFactory.getSingleton(null);
		svc.saveMonitor(playMonitor);
		
		List<String> ids = schemaDbo.getMonitorIds();
		ids.add(playMonitor.getId());
		
		NoSql.em().put(schemaDbo);
		NoSql.em().flush();

		dbProperties(schema);
	}

	public static void dbUserAdd(String schemaName, String type) {
		EntityUser user = Utility.getCurrentUser(session);
		SecureSchema schema = schemaCheck(schemaName, user, PermissionType.ADMIN);
		render("@dbUserEdit", schema, type);
	}
	
	public static void dbUserEdit(String schemaName, String type, String id) {
		EntityUser user = Utility.getCurrentUser(session);
		SecureSchema schema = schemaCheck(schemaName, user, PermissionType.ADMIN);

		SecureResourceGroupXref xref = null;
		List<SecureResourceGroupXref> xrefs = schema.getEntitiesWithAccess();
		for(SecureResourceGroupXref ref : xrefs) {
			Entity entity = ref.getUserOrGroup();
			if(entity.getName().equals(id)) {
				if("group".equals(type) && entity instanceof EntityGroup) {
					xref = ref;
					break;
				} else if("user".equals(type) && entity instanceof EntityUser) {
					xref = ref;
					break;
				}
			}
		}

		if(xref == null)
			notFound("sorry, you might have a bad url or it could be a bug too");
		
		String permission = xref.getPermission().value();
		render(schema, type, xref, permission);
	}
	
	public static void postUserDb(String schemaName, String type, String name, String xrefId, String permission) {
		EntityUser user = Utility.getCurrentUser(session);
		SecureSchema schema = schemaCheck(schemaName, user, PermissionType.ADMIN);

		PermissionType p = PermissionType.lookup(permission);
		SecureResourceGroupXref xref;
		if(xrefId == null || "".equals(xrefId)) {
			Entity entity;
			if("group".equals(type)) {
				entity = EntityGroup.findByName(NoSql.em(), name);
				if (entity == null) {
					flash.error("The group="+name+" does not exist");
					flash.keep();
					MyDatabases.dbUserAdd(schemaName, type);
				}
			} else {
				entity = EntityUser.findByName(NoSql.em(), name);
				if (entity == null) {
					flash.error("The user="+name+" does not exist.  Please ask them to login to the system once first!!!");
					flash.keep();
					MyDatabases.dbUserAdd(schema.getSchemaName(), type);
				}
			}

			xref = new SecureResourceGroupXref(entity, schema, p);
			xref.setPermission(p);
			NoSql.em().put(xref);
			NoSql.em().put(schema);
			NoSql.em().put(user);
		} else {
			
			for(SecureResourceGroupXref ref : schema.getEntitiesWithAccess()) {
				Entity ent = ref.getUserOrGroup();
				if(ent.getName().equals(name)) {
					if(ent instanceof EntityGroup && "group".equals(type)) {
						flash.error("The group="+name+" already has access to this database");
						flash.keep();
						MyDatabases.dbUserAdd(schemaName, type);
					} else if(ent instanceof EntityUser && "user".equals(type)) {
						flash.error("The user="+name+" already has access to this database");
						flash.keep();
						MyDatabases.dbUserAdd(schemaName, type);
					}
				}
			}
			
			xref = NoSql.em().find(SecureResourceGroupXref.class, xrefId);
			xref.setPermission(p);
			NoSql.em().put(xref);
		}

		NoSql.em().flush();

		dbUsers(schemaName);
	}

}
