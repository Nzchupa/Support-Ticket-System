package database;

import java.sql.Connection;

import controller.AuthController;
import model.UserRole;

public class DatabaseTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		   System.out.println("Verbinde mit Datenbank...");

	        // 1. Verbindung testen
	        Connection con = DatabaseConnection.getInstance().getConnection();

	        if (con != null) {
	            System.out.println("✅ Verbindung erfolgreich!");
	        } else {
	            System.out.println("❌ Verbindung fehlgeschlagen!");
	            return;
	        }

	        // 2. User laden + User hinzufuegen, testen
	        UserDAO dao = new UserDAO();
	        
	        boolean registered = dao.insert("test@gmai.com", "ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae", UserRole.USER);
	        
	        if(registered) {
	        	System.out.println("User erfolgreich hinzugefuegt");
	        }else {
	        	System.out.println("❌ Registrierung fehlgeschlagen – Email schon vorhanden?");
	        }
	        
	        var users = dao.findAll();

	        System.out.println("✅ Benutzer in der Datenbank: " + users.size());
	        for (var user : users) {
	            System.out.println("   → " + user.getEmail() + " | " + user.getRole());
	        }

	        // 3. Admin-Login testen
	        var admin = dao.findByEmail("admin@ticket-system.de");
	        if (admin.isPresent()) {
	            System.out.println("✅ Admin-Account gefunden: " + admin.get().getEmail());
	        } else {
	            System.out.println("❌ Admin-Account nicht gefunden – setup.sql ausgeführt?");
	        }

	        DatabaseConnection.getInstance().close();
	}

}
