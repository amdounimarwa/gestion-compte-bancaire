package org.sid.metier;

import java.util.Date;

import javax.transaction.Transactional;

import org.sid.dao.CompteRepository;
import org.sid.dao.OperationRepository;
import org.sid.entities.Compte;
import org.sid.entities.CompteCourant;
import org.sid.entities.Operation;
import org.sid.entities.Retrait;
import org.sid.entities.Versement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
@Service
@Transactional

public class BanqueMetierImpl implements IBanqueMetier {
	
	
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;

	@Override
	public Compte consulterCompte(String Codecpte) {
		Compte cp= compteRepository.findById(Codecpte).orElse(null);
		if(cp==null) throw new RuntimeException("compte introuvable");
		
		return cp;
	}

	@Override
	public void verser(String Codecpte, double montant) {
		Compte cp=consulterCompte(Codecpte);
		Versement V= new Versement(new Date(), montant, cp );
		operationRepository.save(V);
		cp.setSolde(cp.getSolde()+montant);
		compteRepository.save(cp);
			
		
	}

	@Override
	public void retirer(String Codecpte, double montant) {
		 Compte cp= consulterCompte(Codecpte);
		  double facilitiesCaisse=0;
		  if(cp instanceof CompteCourant);
		  facilitiesCaisse=((CompteCourant) cp).getDecouvert();
		  if(cp.getSolde()+facilitiesCaisse<montant)
			  throw new RuntimeException("solde insuffisant");
		   Retrait r= new Retrait(new Date(), montant, cp);
		   operationRepository.save(r);
		   cp.setSolde((cp.getSolde()-montant));
		   compteRepository.save(cp);
		   
			  
		
	}

	@Override
	public void virer(String Codecpte1, String Codecpte2, double montant) {
		
		retirer(Codecpte1, montant);
		verser(Codecpte2, montant);
		
		
	}

	@Override
	public Page <Operation> listeOperation(String Codecpte, int page, int size){
		return operationRepository.listOperation(Codecpte, PageRequest.of(page, size));
	}

}
