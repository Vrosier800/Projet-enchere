package fr.eni.projet.ProjetEnchere.bll;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MyScheduledTask {
	
	private ArticleAVendreService articleService;
	
	
	
    public MyScheduledTask(ArticleAVendreService articleService) {
		this.articleService = articleService;
	}

//    pour tester le fonctionnement de la méthode de tâche journalière au démarrage du serveur
	@PostConstruct
    public void auDemarrage() {
        tacheJournaliere();
    }

    @Scheduled(cron = "00 00 00 * * *") 
    public void tacheJournaliere() {
        System.out.println("Tâche exécutée à : " + java.time.LocalDateTime.now());
        articleService.miseAJourStatut();
        
    }
	
}
