(function ($) {
    var Person = function (data) {
        var self = this,
            i;

        self.age = data.hasOwnProperty('age') ? data.age : null;
        self.firstName = data.hasOwnProperty('firstName') ? data.firstName : null;
        self.gender = data.hasOwnProperty('gender') ? data.gender : null;
        self.homeAddress = data.hasOwnProperty('homeAddress') ? data.homeAddress : null;
        self.lastName = data.hasOwnProperty('lastName') ? data.lastName : null;
        self.passportId = data.hasOwnProperty('passportId') ? data.passportId : null;

        self.fullName = self.firstName + ' ' + self.lastName;

        self.links = {};
        if ((data.hasOwnProperty('_links')) && ($.isArray(data._links)) && (data._links.length > 0)) {
            for (i = 0; i < data._links.length; i += 1) {
                if ((data._links [i].hasOwnProperty('rel')) && (data._links [i].hasOwnProperty('href'))) {
                    self.links [data._links [i].rel] = data._links [i].href;
                }
            }
        }
    };

    var People = function (collectionData) {
        var self = this,
            totalPages, i;

        if (collectionData.hasOwnProperty('_metadata')) {
            self.count = collectionData._metadata.hasOwnProperty('count') ? collectionData._metadata.count : null;
            self.page = collectionData._metadata.hasOwnProperty('page') ? collectionData._metadata.page : null;
            self.size = collectionData._metadata.hasOwnProperty('size') ? collectionData._metadata.size : null;
            self.total = collectionData._metadata.hasOwnProperty('total') ? collectionData._metadata.total : null;
            totalPages = Math.ceil(self.total / self.size);
            self.hasNext = self.page < totalPages;
        } else {
            self.count = null;
            self.page = null;
            self.size = null;
            self.total = null;
            self.hasNext = false;
        }

        self.links = {};
        if ((collectionData.hasOwnProperty('_links')) && ($.isArray(collectionData._links)) && (collectionData._links.length > 0)) {
            for (i = 0; i < collectionData._links.length; i += 1) {
                if ((collectionData._links [i].hasOwnProperty('rel')) && (collectionData._links [i].hasOwnProperty('href'))) {
                    self.links [collectionData._links [i].rel] = collectionData._links [i].href;
                }
            }
        }

        self.items = [];
        if ((collectionData.hasOwnProperty('items')) && ($.isArray(collectionData.items)) && (collectionData.items.length > 0)) {
            for (i = 0; i < collectionData.items.length; i += 1) {
                self.items.push(new Person(collectionData.items [i]));
            }
        }


    };

    var PeopleViewModel = function () {
        var self = this;

        self.endRecordNumber = ko.observable(null);
        self.hasNext = ko.observable(false);
        self.nextUrl = ko.observable(null);
        self.totalRecords = ko.observable(null);
        self.people = ko.observableArray([]);

        var onFetchPeopleSuccessHandler = function (response) {
            var json = ko.toJSON(response),
                data = JSON.parse(json),
                people = new People(data);

            self.people($.merge (self.people(), people.items));
            self.endRecordNumber(((people.page - 1) * people.size) + Math.min(people.size, people.count));
            self.totalRecords(people.total);
            self.hasNext (people.hasNext);
            self.nextUrl (people.links.hasOwnProperty ('next') ? people.links.next : null);
        };

        self.fetchAll = function () {
            $.ajax('api/people', {
                type: "get",
                success: onFetchPeopleSuccessHandler
            });
        };

        self.fetchNext = function () {
            $.ajax(self.nextUrl (), {
                type: "get",
                success: onFetchPeopleSuccessHandler
            });
        };

        self.fetchAll();
    };

    ko.applyBindings(new PeopleViewModel());
}($));