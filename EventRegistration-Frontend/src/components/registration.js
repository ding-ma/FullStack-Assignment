import axios from 'axios';

let config = require('../../config');

let backendConfigurer = function () {
  switch (process.env.NODE_ENV) {
    case 'testing':
    case 'development':
      return 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;
    case 'production':
      return 'https://' + config.build.backendHost + ':' + config.build.backendPort;
  }
};

let backendUrl = backendConfigurer();

let AXIOS = axios.create({
  baseURL: backendUrl
  // headers: {'Access-Control-Allow-Origin': frontendUrl}
});

export default {
  name: 'eventregistration',
  data () {
    return {
      persons: [],
      volunteers: [],
      events: [],
      payments: [],
      newPerson: '',
      personType: 'Person',
      newEvent: {
        name: '',
        date: '2017-12-08',
        startTime: '09:00',
        endTime: '11:00'
      },
      company: '',
      newCompany: '',
      selectedPerson: '',
      selectedVolunteer: '',
      selectedEvent: '',
      payment: {
        accountNumber: '',
        amount: ''
      },
      errorPerson: '',
      errorEvent: '',
      errorRegistration: '',
      errorAssignProfessional: '',
      errorPayment: '',
      response: []
    }
  },
  created: function () {
    // Initializing persons
    AXIOS.get('/persons')
      .then(response => {
        this.persons = response.data;
        this.persons.forEach(person => this.getRegistrations(person.name))
      })
      .catch(e => {
        this.errorPerson = e
      });

    AXIOS.get('/events').then(response => {
      this.events = response.data
    }).catch(e => {
      this.errorEvent = e
    });

    AXIOS.get('/events/companies').then(response => {
      this.events = this.events.concat(response.data)
    }).catch(e => {
      this.errorEvent = e
    });

    AXIOS.get('/volunteers')
      .then(response => {
        this.volunteers = response.data;
        this.volunteers.forEach(person => this.getRegistrations(person.name))
      })
      .catch(e => {
        this.errorPerson = e
      });
    // get payments here
  },

  methods: {

    createPerson: function (personType, personName) {
      if (personType === 'Person') {
        AXIOS.post('/persons/'.concat(personName), {}, {})
          .then(response => {
            this.persons.push(response.data);
            this.errorPerson = '';
            this.newPerson = '';
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorPerson = e;
            console.log(e);
          });
      } else {
        AXIOS.post('/volunteer/'.concat(personName), {}, {})
          .then(response => {
            this.volunteers.push(response.data);
            this.persons.push(response.data);
            this.errorPerson = '';
            this.newPerson = '';
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorAssignProfessional = e;
            console.log(e);
          });
      }
    },

    createEvent: function (newEvent, company) {
      if (company === '') {
        AXIOS.post('/events/'.concat(newEvent.name), {}, {params: newEvent})
          .then(response => {
            this.events.push(response.data);
            this.errorEvent = '';
            this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorEvent = e;
            console.log(e);
          });
      } else {
        AXIOS.post('/events/'.concat(newEvent.name).concat('/company/').concat(company), {}, {params: newEvent})
          .then(response => {
            this.events.push(response.data);
            this.errorEvent = '';
            this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorEvent = e;
            console.log(e);
          });
      }
    },

    registerEvent: function (personName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === personName);
      let params = {
        person: person.name,
        event: event.name
      };

      AXIOS.post('/register', {}, {params: params})
        .then(response => {
          person.eventsAttended.push(event);
          this.selectedPerson = '';
          this.selectedEvent = '';
          this.errorRegistration = '';
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorRegistration = e;
          console.log(e);
        });
    },

    assignProfessional: function (volunteerName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === volunteerName);
      AXIOS.post('/volunteer/' + volunteerName + '/event/' + eventName, undefined, undefined)
        .then(response => {
          person.eventsAttended.push(event);
          this.selectedPerson = '';
          this.selectedEvent = '';
          this.errorAssignProfessional = '';
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorAssignProfessional = e;
          console.log(e);
        });
    },

    getRegistrations: function (personName) {
      AXIOS.get('/events/person/'.concat(personName))
        .then(response => {
          if (!response.data || response.data.length <= 0) return;

          let indexPart = this.persons.map(x => x.name).indexOf(personName);
          this.persons[indexPart].eventsAttended = [];
          response.data.forEach(event => {
            this.persons[indexPart].eventsAttended.push(event);
          });
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          console.log(e);
        });
    },

    makePayment: function (personName, eventName, paymentAccount, paymentAmount) {
      this.errorPayment = '';
      console.log(personName, eventName, paymentAccount, paymentAmount);
      let params = {
        accountNumber: paymentAccount,
        amount: paymentAmount
      };
      AXIOS.post('payment/'.concat(personName).concat('/').concat(eventName), undefined, {params: params})
        .then(response => {
          this.payments.push(response.data);
          console.log(this.payments);
          //   console.log(this.persons);
          //   console.log(response.data);
          // //  this.persons.events.push(response.data.creditCardDto)
          //   console.log(this.persons[0].eventsAttended.push(response.data.creditCardDto.amount))
          //   console.log(this.persons)
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorPayment = e;
          console.log(e);
        });
    },

    getPayment: function (personName, eventName) {

    }
  }
}
