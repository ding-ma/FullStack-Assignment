<template>
  <div id="eventregistration">
    <h2>Persons</h2>
    <table id="persons-table">
      <tr>
        <th>Name</th>
        <th>Events</th>
        <th>Payment ID</th>
        <th>Amount ($)</th>
      </tr>
      <tr v-for="(person, i) in persons" v-bind:key="`person-${i}`">
        <td>{{person.name}}</td>
        <td>
          <ul>
            <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`" style="list-style-type: disc;">
              <span class='registration-event-name'>{{event.name}}</span>
            </li>
          </ul>
        </td>
        <td>
          <div style="list-style-type: disc;" v-bind:key="`event-${i}`" v-for="(payment, i) in person.payments">
            <span class='registration-event-name'>{{payment.accountNumber}}</span>
          </div>
        </td>
        <td>
          <div style="list-style-type: disc;" v-bind:key="`event-${i}`" v-for="(payment, i) in person.payments">
            <span class='registration-event-name'>{{payment.amount}}</span>
          </div>
        </td>
      </tr>
      <tr>
        <td>
          <input id="create-person-person-name" placeholder="Person Name" type="text" v-model="newPerson">
        </td>
        <td>
          <select id='create-person-person-type' v-model="personType">
            <option>Person</option>
            <option>Volunteer</option>
          </select>
        </td>
        <td>
          <button @click="createPerson(personType, newPerson)" id="create-person-button" v-bind:disabled="!newPerson">
            Create Person
          </button>
        </td>
        <td></td>
        <td></td>
      </tr>
    </table>
    <span v-if="errorPerson" style="color:red">Error: {{errorPerson}}</span>
    <hr>
    <h2>Events</h2>
    <table id='events-table'>
      <tr>
        <th>Name</th>
        <th>Date</th>
        <th>Start</th>
        <th>End</th>
        <th>Company</th>
      </tr>
      <tr v-for="(event, i) in events" v-bind:id="event.name" v-bind:key="`event-${i}`">
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-name`">{{event.name}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-date`">{{event.date}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-starttime`">{{event.startTime}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-endtime`">{{event.endTime}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-company`">{{event.company}}</td>
      </tr>
      <tr>
        <td>
          <input id="event-name-input" type="text" v-model="newEvent.name" placeholder="Event Name">
        </td>
        <td>
          <input id="event-date-input" type="date" v-model="newEvent.date" placeholder="YYYY-MM-DD">
        </td>
        <td>
          <input id="event-starttime-input" type="time" v-model="newEvent.startTime" placeholder="HH:mm">
        </td>
        <td>
          <input id="event-endtime-input" type="time" v-model="newEvent.endTime" placeholder="HH:mm">
        </td>
        <td>
          <input id="event-company-input" placeholder="Company" type="text" v-model="newEvent.company">
        </td>
        <td>
          <button id="event-create-button" v-bind:disabled="!newEvent.name"
                  v-on:click="createEvent(newEvent)">
            Create
          </button>
        </td>
      </tr>
    </table>
    <span id="event-error" v-if="errorEvent" style="color:red">Error: {{errorEvent}}</span>
    <hr>


    <h2>Registrations</h2>
    <label>Person:
      <select id='registration-person-select' v-model="selectedPerson">
        <option disabled value="">Please select one</option>
        <option v-bind:key="`person-${i}`" v-for="(person, i) in persons">{{person.name}}</option>
      </select>
    </label>
    <label>Event:
      <select id='registration-event-select' v-model="selectedEvent">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
      </select>
    </label>
    <button @click="registerEvent(selectedPerson, selectedEvent)" id='registration-button'
            v-bind:disabled="!selectedPerson || !selectedEvent">Register
    </button>
    <br/>
    <span v-if="errorRegistration" style="color:red">Error: {{errorRegistration}}</span>
    <hr>


    <h2>Assign Professional</h2>
    <label>Volunteer:
      <select id='assign-selected-volunteer' v-model="selectedVolunteer">
        <option disabled value="">Please select one</option>
        <option v-bind:key="`volunteer-${i}`" v-for="(volunteer, i) in volunteers">{{volunteer.name}}</option>
      </select>
    </label>
    <label>Event:
      <select id='assign-selected-event-volunteer' v-model="selectedEvent">
        <option disabled value="">Please select one</option>
        <option v-bind:key="`event-${i}`" v-for="(event, i) in events">{{event.name}}</option>
      </select>
    </label>
    <button @click="assignProfessional(selectedVolunteer, selectedEvent)" id='assign-button-volunteer'
            v-bind:disabled="!selectedVolunteer || !selectedEvent">Assign
    </button>
    <br/>
    <span v-if="errorAssignProfessional" style="color:red">Error: {{errorAssignProfessional}}</span>
    <hr>


    <h2>Pay for Registration with CreditCard</h2>
    <table align="center">
      <tr>
        <label>Person:
          <select id='credit-card-person-select' v-model="selectedPerson">
            <option disabled value="">Please select one</option>
            <option v-bind:key="`person-${i}`" v-for="(person, i) in persons">{{person.name}}</option>
          </select>
        </label>
        <label>Event:
          <select id='credit-card-event-select' v-model="selectedEvent">
            <option disabled value="">Please select one</option>
            <option v-bind:key="`event-${i}`" v-for="(event, i) in events">{{event.name}}</option>
          </select>
        </label>
      </tr>
      <tr>
        <label>Device Id:
          <input id='credit-card-id-input' placeholder="DDDD-DDDD" type="text" v-model="payment.accountNumber">
        </label>
        <label>Amount:
          <input id='credit-card-amount-input' placeholder="0$" type="number" v-model="payment.amount">
        </label>
      </tr>
      <tr>
        <button @click="makePayment(selectedPerson,selectedEvent,payment.accountNumber, payment.amount)"
                id='credit-card-button'
                v-bind:disabled="!selectedPerson || !selectedEvent || !payment">Make Payment
        </button>
        <br/>
        <span id="credit-card-error" style="color:red" v-if="errorPayment">Error: {{errorPayment}}</span>
      </tr>
    </table>
    <hr>
  </div>
</template>

<script src="./registration.js"></script>


<style>
  #eventregistration {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    background: #f2ece8;
    margin-top: 60px;
  }

  .registration-event-name {
    display: inline-block;
    width: 25%;
  }

  .registration-event-name {
    display: inline-block;
  }

  h1, h2 {
    font-weight: normal;
  }

  ul {
    list-style-type: none;
    text-align: left;
  }

  a {
    color: #42b983;
  }
</style>
