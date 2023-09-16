Vue.createApp({
    data() {
        return {
            clientInfo: {},
            selectDeleteCard: [],
            creditCards: [],
            debitCards: [],
            activeCards: [],
            activeCreditCards: [],
            actualDate: new Date(),
            activeDebitCards: [],
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                    this.creditCards = this.clientInfo.cards.filter(card => card.type == "CREDIT");
                    this.debitCards = this.clientInfo.cards.filter(card => card.type == "DEBIT");
                    this.activeDebitCards = this.debitCards.filter(card => card.active == true);
                    this.activeCreditCards = this.creditCards.filter(card => card.active == true);
                    this.activeCards = this.clientInfo.cards.filter(card => card.active == true);
                    console.log(this.actualDate)

                })
                .catch((error) => {
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        deleteCard: function () {
           //axios.patch("/api/clients/current/cards?" + `cardNumber=${this.selectDeleteCard}`)
           axios.patch(`/api/clients/current/cards?cardNumber=${this.selectDeleteCard}`)
           .then((response) => {
                    this.modal.hide();
                    this.okmodal.show();

                })
                 .catch((error) => {
                    console.log(error);
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        },
        confirmDelete: function(){
            this.modal.show();
        },
        finish: function () {
                    window.location.reload();
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
        this.getData();
    }
}).mount('#app');