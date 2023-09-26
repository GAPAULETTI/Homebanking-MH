Vue.createApp({
    data(){
        return{
              loanInfo: [],
              loanId: 0,
              loanName: "",
              maxAmount: 0,
              interest: 0,
              payments: 0,
              payments2: [],
              errorToats: null,
              errorMsg: null
        }
    },
    methods: {
        getData: function(){
            axios.get("/api/loans")
            .then((response) => {
                this.loanInfo = response.data;
                this.payments2 = this.loanInfo.map(payment => payment.payments);
                console.log(this.payments2)

                console.log(this.loanInfo)
            })
            .catch((error) => {
                                console.log(error);
                                this.errorMsg = error.response.data;
                                this.errorToats.show();
            })
            //axios.put('/api/loans', {name:this.loanName , maxAmount:this.maxAmount , payments: this.payments , interestLoan : this.interest})
            },
            create: function(){
            axios.put(`/api/loans?name=${this.loanName}&maxAmount=${this.maxAmount}&payments=${this.payments}&interestLoan=${this.interest}`)
            .then( (response) => {
                window.location.reload();
            })
             .catch((error) => {
                            console.log(error);
                            this.errorMsg = error.response.data;
                            this.errorToats.show();
            })
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
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount("#appAdmin");

// let config = {
                         //   headers: {
                               // 'content-type': 'application/x-www-form-urlencoded'
                          //  }
                       // }
            //axios.put(`/api/loans?name=${this.loanName}&maxAmount=${this.maxAmount}&payments=${this.payments}&interestLoan=${this.interest}`, config)