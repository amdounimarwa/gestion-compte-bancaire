package org.sid.metier;

import org.sid.entities.Compte;
import org.sid.entities.Operation;
import org.springframework.data.domain.Page;

public interface IBanqueMetier {
	public Compte consulterCompte(String Codecpte);
	public void verser(String Codecpte, double montant);
	public void retirer(String Codecpte, double montant);
	public void virer(String Codecpte1, String Codecpte2, double montant );
	public Page<Operation> listeOperation(String Codecpte, int page, int size);
	
	

}
