document.addEventListener('DOMContentLoaded', function() {
	let calendarEl = document.getElementById('calendar');

	const userLocale = localStorage.getItem('userLanguage');


	const locale = userLocale || 'fr';


	let calendar = new FullCalendar.Calendar(calendarEl, {
		initialView: 'dayGridMonth',
		events: '/api/articles',
		locale: userLocale,
		firstDay: '1',
		eventColor: 'rgba(120, 183, 131)',
		eventContent: function(arg) {
			let contentEl = document.createElement('div');
			let titleEl = document.createElement('div');
			titleEl.textContent = arg.event.title;
			contentEl.appendChild(titleEl);

			return { domNodes: [contentEl] };
		},


		eventClick: function(info) {

			let existingMenu = document.getElementById('eventOptions');
			if (existingMenu) {
				existingMenu.remove();
			}


			let messages = document.getElementById('messages');

			if (!messages) {
				return;
			}

			let enchereAfficher = messages.children[2] ? messages.children[2].innerText : 'Afficher';
			let enchereFaireOffre = messages.children[3] ? messages.children[3].innerText : 'Enchérir';
			let enchereConfirmation = messages.children[4] ? messages.children[4].innerText : 'Voulez-vous vraiment faire une offre ?';
			let enchereCreer = messages.children[5] ? messages.children[5].innerText : 'Entrer le montant de l\'enchère';

			

			let options = `
		        <div id="eventOptions" style="position:absolute; background-color:#fff; border:1px solid #ccc; padding:10px; z-index:1000;">
		            <button id="afficher">${enchereAfficher}</button>
		            <button id="encherir">${enchereFaireOffre}</button>
		        </div>`;

			document.body.insertAdjacentHTML('beforeend', options);


			let eventOptionsEl = document.getElementById('eventOptions');
			eventOptionsEl.style.left = `${info.jsEvent.pageX}px`;
			eventOptionsEl.style.top = `${info.jsEvent.pageY}px`;

			info.jsEvent.preventDefault();
			info.jsEvent.stopPropagation();


			document.getElementById('afficher').addEventListener('click', function(event) {
				event.stopPropagation();

				const articleId = info.event.id;
				window.location.href = `/article/detail?id=${articleId}`;
			});

			let utilisateur;

			fetch('/api/utilisateur')
				.then(response => {
					if (response.ok) {
						return response.json();
					}
					throw new Error('Utilisateur non connecté');
				})
				.then(data => {
					utilisateur = data;
				})
				.catch(error => {
				});

			let csrfToken = document.querySelector('meta[name="_csrf"]').content;
			let csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
			let enchereSuccessMessage = messages.children[0].innerText;
			let enchereEchoueeMessage = messages.children[1].innerText;


			document.getElementById('encherir').addEventListener('click', function(event) {
				event.stopPropagation();
				if (confirm(enchereConfirmation)) {
					var montant = prompt(enchereCreer);

					if (montant) {
						let dateActuelle = new Date().toISOString().split('T')[0];

						if (typeof utilisateur === 'undefined' || !utilisateur.pseudo) {
							return;
						}
						var params = new URLSearchParams();
						params.append('montant', parseInt(montant));
						params.append('date', dateActuelle);
						params.append('id', info.event.id);
						params.append('pseudo', utilisateur.pseudo);
						fetch('/api/encherir', {
							method: 'POST',
							headers: {
								[csrfHeader]: csrfToken,
								"charset": "UTF-8",
								'Content-Type': 'application/x-www-form-urlencoded'
							},
							body: params.toString()
						})
							.then(response => {
								if (!response.ok) {
									throw new Error('Erreur lors du chargement de l\'enchère');
								}
								return response.json();
							})
							.then(data => {
								window.location.reload();
								alert(enchereSuccessMessage);
							})
							.catch(error => {

								alert(enchereEchoueeMessage);
							});
					}
				}
			});
			document.addEventListener('click', function(event) {
				if (!eventOptionsEl.contains(event.target)) {
					eventOptionsEl.remove();
				}
			}, { once: true });
		},


		eventMouseEnter: function(info) {
			let enchereDescription = messages.children[6] ? messages.children[6].innerText : 'Description :';
			let enchereDebutEnchere = messages.children[7] ? messages.children[7].innerText : 'Début enchères :';
			let enchereFinEnchere = messages.children[8] ? messages.children[8].innerText : 'Fin enchères :';
			let encherePrixInit = messages.children[9] ? messages.children[9].innerText : 'Prix initial :';
			let enchereMeilleurEnchere = messages.children[10] ? messages.children[10].innerText : 'Meilleure enchère  :';
			let enchereMeilleurEncherisseur = messages.children[11] ? messages.children[11].innerText : 'Meilleur(e) encherisseu.r.se :';
			let enchereEncherisseurNull = messages.children[12] ? messages.children[12].innerText : 'Aucun encherisseur';
			let tooltip = document.createElement('div');
			
			tooltip.setAttribute('id', 'tooltip');
			tooltip.innerHTML = `
                <strong>${info.event.title}</strong><br>
				${enchereDescription} ${info.event.extendedProps.description}<br>
				${enchereDebutEnchere} ${info.event.start.toLocaleDateString()}<br>
				${enchereFinEnchere} ${info.event.end.toLocaleDateString()}<br>
				${encherePrixInit} ${info.event.extendedProps.prixInitial}<br>
				${enchereMeilleurEnchere} ${info.event.extendedProps.enchereMax}<br>
				${enchereMeilleurEncherisseur}	${info.event.extendedProps.acquereur != null ? info.event.extendedProps.acquereur : enchereEncherisseurNull} `;
			tooltip.style.position = 'absolute';
			tooltip.style.background = 'rgba(120, 183, 131, 0.5)';
			tooltip.style.border = '1px solid #5a8f63';
			tooltip.style.borderRadius = '20px';
			tooltip.style.padding = '10px';
			tooltip.style.zIndex = '1000';
			tooltip.style.left = `${info.jsEvent.pageX + 10}px`;
			tooltip.style.top = `${info.jsEvent.pageY + 10}px`;

			document.body.appendChild(tooltip);
		},

		eventMouseLeave: function(info) {
			let tooltip = document.getElementById('tooltip');
			if (tooltip) {
				tooltip.remove();
			}
		}
	});

	calendar.render();
});