import { Selector } from 'testcafe';

const loginLink = Selector('a[href="/login"]');
const signUpLink = Selector('a[href="/users/new"]');
const loginForm = Selector('form[action="/login"]');

const yourListingsLink = Selector('a[href="/apartments/mine"]');
const addListingLink = Selector('a[href="/apartments/new"]');
const logoutButton = Selector('button').withText('Logout');

const signUpEmail = Selector('input[name="email"][type="email"]');
const signUpPassword = Selector('input[name="password"][type="password"]');
const signUpFirstName = Selector('input[name="first_name"][type="text"]');
const signUpLastName = Selector('input[name="last_name"][type="text"]');
const signUpButton = Selector('button').withText('Sign up');

const loginEmail = signUpEmail;
const loginPassword = signUpPassword;
const loginButton = Selector('button').withText('Login');

const rentInput = Selector('input[name="rent"][type="number"]');
const numberOfBathroomsInput = Selector('input[name="number_of_bathrooms"][type="number"]');
const numberOfBedroomsInput = Selector('input[name="number_of_bedrooms"][type="number"]');
const squareFootageInput = Selector('input[name="square_footage"][type="number"]');
const addressInput = Selector('input[name="address"][type="text"],input[name="street"][type="text"]');
const cityInput = Selector('input[name="city"][type="text"]');
const stateInput = Selector('input[name="state"][type="text"]');
const zipInput = Selector('input[name="zip_code"][type="text"]');
const addListingButton = Selector('button').withText('Add listing');

const billie = { email: Math.random() * 10e5 + "@example.com", password: "password1", firstName: "Billie", lastName: "Mulligan" };
const monica = { email: Math.random() * 10e5 + "@example.com", password: "password2", firstName: "Monica", lastName: "Evers" };

const prop1 = { rent: 10, baths: 10, beds: 10, sqft: 10, address: '10 10th St Apt #' + Math.floor(Math.random() * 10e5), city: 'Tensville', state: 'TN', zip: '10101' };

fixture`Common Navigation (when logged out)`
  .page`http://localhost:4567/`;

test('Login link shows up on home page', async t => {
  await t
    .expect(loginLink.exists).ok('Could not find login link on the home page')
    .expect(loginLink.innerText).eql('Login', 'Login link does not read "Login" on the home page');
});

test('Sign up link shows up on home page', async t => {
  await t
    .expect(signUpLink.exists).ok('Could not find sign-up link on the home page')
    .expect(signUpLink.innerText).eql('Sign up', 'Sign-up link does not read "Sign up" on the home page');
});

test('Login link shows up on login page', async t => {
  await t
    .navigateTo('/login')
    .expect(loginLink.exists).ok('Could not find login link on the login page')
    .expect(loginLink.innerText).eql('Login', 'Login link does not read "Login" on the login page');
});

test('Sign up link shows up on login page', async t => {
  await t
    .navigateTo('/login')
    .expect(signUpLink.exists).ok('Could not find sign-up link on the login page')
    .expect(signUpLink.innerText).eql('Sign up', 'Sign-up link does not read "Sign up" on the login page');
});

test('Login link shows up on sign-up page', async t => {
  await t
    .navigateTo('/users/new')
    .expect(loginLink.exists).ok('Could not find login link on the sign-up page')
    .expect(loginLink.innerText).eql('Login', 'Login link does not read "Login" on the sign-up page');
});

test('Sign up link shows up on sign-up page', async t => {
  await t
    .navigateTo('/users/new')
    .expect(signUpLink.exists).ok('Could not find sign-up link on the sign-up page')
    .expect(signUpLink.innerText).eql('Sign up', 'Sign-up link does not read "Sign up" on the sign-up page');
});

fixture`Protected Pages (when logged out)`
  .page`http://localhost:4567/`;

test('Cannot access "Your listings" page when not authenticated', async t => {
  await t
    .navigateTo('http://localhost:4567/apartments/mine')
    .expect(loginForm.exists).ok('Failed to redirect to login page when accessing "Your listings" without logging in.');
});

test('Cannot access "Create apartment" page when not authenticated', async t => {
  await t
    .navigateTo('http://localhost:4567/apartments/new')
    .expect(loginForm.exists).ok('Failed to redirect to login page when accessing "Your listings" without logging in.');
});

fixture`Signing up`
  .page`http://localhost:4567/users/new`;

test('Sign up page has an email input', async t => {
  await t
    .expect(signUpEmail.exists).ok('Could not find an email input named "email"');
});

test('Sign up page has a password input', async t => {
  await t
    .expect(signUpPassword.exists).ok('Could not find a password input named "password"');
});

test('Sign up page has a first name input', async t => {
  await t
    .expect(signUpFirstName.exists).ok('Could not find a text input named "first_name"');
});

test('Sign up page has a last name input', async t => {
  await t
    .expect(signUpLastName.exists).ok('Could not find a text input named "last_name"');
});

test('Sign up page has a button to submit the form', async t => {
  await t
    .expect(signUpButton.exists).ok('Could not find a button with text "Sign up"');
});

test('Filling out fields and submitting them let me login', async t => {
  await t
    .typeText(signUpEmail, billie.email)
    .typeText(signUpPassword, billie.password)
    .typeText(signUpFirstName, billie.firstName)
    .typeText(signUpLastName, billie.lastName)
    .click(signUpButton)
    .expect(Selector('h1').innerText).eql('Apartments!', 'It does not seem that we\'ve arrived on the home page that should have a header named "Apartments!"');
});

test('Filling out fields as someone else and submitting them let me login', async t => {
  await t
    .typeText(signUpEmail, monica.email)
    .typeText(signUpPassword, monica.password)
    .typeText(signUpFirstName, monica.firstName)
    .typeText(signUpLastName, monica.lastName)
    .click(signUpButton)
    .expect(Selector('h1').innerText).eql('Apartments!', 'It does not seem that we\'ve arrived on the home page that should have a header named "Apartments!"');
});

fixture`Login in works`
  .page`http://localhost:4567/login`;

test('Login page has an email input', async t => {
  await t
    .expect(loginEmail.exists).ok('Could not find an email input named "email"');
});

test('Login page has a password input', async t => {
  await t
    .expect(loginPassword.exists).ok('Could not find a password input named "password"');
});

test('Login page has a login button', async t => {
  await t
    .expect(loginButton.exists).ok('Could not find a button with the text "Login"');
});

test('Logging in takes the user to the home page', async t => {
  await t
    .typeText(loginEmail, billie.email)
    .typeText(loginPassword, billie.password)
    .click(loginButton)
    .expect(Selector('h1').innerText).eql('Apartments!', 'It does not seem that we\'ve arrived on the home page that should have a header named "Apartments!"');
});

fixture`Common Navigation (when logged in)`
  .page`http://localhost:4567/login`
  .beforeEach(async t => {
    await t
      .typeText(loginEmail, billie.email)
      .typeText(loginPassword, billie.password)
      .click(loginButton);
  });

test('"Your listings" link shows up on home page', async t => {
  await t
    .expect(yourListingsLink.exists).ok('Could not find "Your listings" link on the home page')
    .expect(yourListingsLink.innerText).eql('Your listings', '"Your listings" link does not read "Your listings" on the home page');
});

test('"Add a listing" link shows up on home page', async t => {
  await t
    .expect(addListingLink.exists).ok('Could not find "Add a listing" link on the home page')
    .expect(addListingLink.innerText).eql('Add a listing', '"Add a listing" link does not read "Add a listing" on the home page');
});

test('Logout button shows up on home page', async t => {
  await t
    .expect(logoutButton.exists).ok('Could not find "Logout" button on the home page')
    .expect(logoutButton.innerText).eql('Logout', '"Logout" button does not read "Logout" on the home page')
});

test('"Your listings" link shows up on "Your listings" page', async t => {
  await t
    .navigateTo('/apartments/mine')
    .expect(yourListingsLink.exists).ok('Could not find "Your listings" link on the "Your listings" page')
    .expect(yourListingsLink.innerText).eql('Your listings', '"Your listings" link does not read "Your listings" on the "Your listings" page');
});

test('"Add a listing" link shows up on "Your listings" page', async t => {
  await t
    .navigateTo('/apartments/mine')
    .expect(addListingLink.exists).ok('Could not find "Add a listing" link on the "Your listings" page')
    .expect(addListingLink.innerText).eql('Add a listing', '"Add a listing" link does not read "Add a listing" on the "Your listings" page');
});

test('Logout button shows up on "Your listings" page', async t => {
  await t
    .navigateTo('/apartments/mine')
    .expect(logoutButton.exists).ok('Could not find "Logout" button on the "Your listings" page')
    .expect(logoutButton.innerText).eql('Logout', '"Logout" button does not read "Logout" on the "Your listings" page')
});

test('"Your listings" link shows up on "Add a listing" page', async t => {
  await t
    .navigateTo('/apartments/new')
    .expect(yourListingsLink.exists).ok('Could not find "Your listings" link on the "Add a listing" page')
    .expect(yourListingsLink.innerText).eql('Your listings', '"Your listings" link does not read "Your listings" on the "Add a listing" page');
});

test('"Add a listing" link shows up on "Add a listing" page', async t => {
  await t
    .navigateTo('/apartments/new')
    .expect(addListingLink.exists).ok('Could not find "Add a listing" link on the "Add a listing" page')
    .expect(addListingLink.innerText).eql('Add a listing', '"Add a listing" link does not read "Add a listing" on the "Add a listing" page');
});

test('Logout button shows up on "Add a listing" page', async t => {
  await t
    .navigateTo('/apartments/new')
    .expect(logoutButton.exists).ok('Could not find "Logout" button on the "Add a listing" page')
    .expect(logoutButton.innerText).eql('Logout', '"Logout" button does not read "Logout" on the "Add a listing" page')
});

fixture`Apartment mangaement`
  .page`http://localhost:4567/apartments/new`
  .beforeEach(async t => {
    await t
      .typeText(loginEmail, billie.email)
      .typeText(loginPassword, billie.password)
      .click(loginButton);
  });

test('Has a number input named "rent"', async t => {
  await t
    .expect(rentInput.exists).ok('Could not find a number input named "rent"');
});

test('Has a number input named "number_of_bathrooms"', async t => {
  await t
    .expect(numberOfBathroomsInput.exists).ok('Could not find a number input named "number_of_bathrooms"');
});

test('Has a number input named "number_of_bedrooms"', async t => {
  await t
    .expect(numberOfBedroomsInput.exists).ok('Could not find a number input named "number_of_bedrooms"');
});

test('Has a number input named "square_footage"', async t => {
  await t
    .expect(squareFootageInput.exists).ok('Could not find a number input named "square_footage"');
});

test('Has a text input named "street" or "address"', async t => {
  await t
    .expect(addressInput.exists).ok('Could not find a text input named "street" or "address"');
});

test('Has a text input named "city"', async t => {
  await t
    .expect(cityInput.exists).ok('Could not find a text input named "city"');
});

test('Has a text input named "state"', async t => {
  await t
    .expect(stateInput.exists).ok('Could not find a text input named "state"');
});

test('Has a text input named "zip_code"', async t => {
  await t
    .expect(zipInput.exists).ok('Could not find a text input named "zip_code"');
});

test('Has a button with the text "Add listing"', async t => {
  await t
    .expect(addListingButton.exists).ok('Could not find a button to add the listing')
    .expect(addListingButton.innerText).eql('Add listing', 'The text of the "Add listing" form does not read "Add listing"');
});

test('Can create a new property and see it in my listings page and on the home page', async t => {
  await t
    .typeText(rentInput, prop1.rent.toString())
    .typeText(numberOfBathroomsInput, prop1.baths.toString())
    .typeText(numberOfBedroomsInput, prop1.beds.toString())
    .typeText(squareFootageInput, prop1.sqft.toString())
    .typeText(cityInput, prop1.city)
    .typeText(stateInput, prop1.state)
    .typeText(zipInput, prop1.zip)
    .typeText(addressInput, prop1.address)
    .click(addListingButton)
    .expect(Selector('a').withText(prop1.address).exists).ok('Could not see the newly created address on my listings page.')
    .navigateTo('/')
    .expect(Selector('a').withText(prop1.address).exists).ok('Could not see the newly created address on the home page.')
});

test('Can toggle state of listing from active to deactive', async t => {
  await t
    .navigateTo('/apartments/mine')
    .click(Selector('a').withText(prop1.address))
    .expect(Selector('button').withText('listing').exists).ok('Could not find a deactivation button for the property')
    .click(Selector('button').withText('listing'))
    .navigateTo('/')
    .expect(Selector('a').withText(prop1.address).exists).notOk('The property is still listed on the home page though it should be deactivated');
});

test('Can toggle state of listing from deactive to active', async t => {
  await t
    .navigateTo('/apartments/mine')
    .click(Selector('a').withText(prop1.address))
    .expect(Selector('button').withText('listing').exists).ok('Could not find an activation button for the property')
    .click(Selector('button').withText('listing'))
    .navigateTo('/')
    .expect(Selector('a').withText(prop1.address).exists).ok('The property is still listed on the home page though it should be deactivated');
});

test('Cannot see LIKE button when logged in as lister', async t => {
  await t
    .navigateTo('/apartments/mine')
    .click(Selector('a').withText(prop1.address))
    .expect(Selector('button').withText('Like this apartment').exists).notOk('Can see the LIKE button when logged in as the lister');
});

test('Cannot see activation button when logged in as a non-lister', async t => {
  await t
  .navigateTo('/login')
  .typeText(loginEmail, monica.email)
  .typeText(loginPassword, monica.password)
  .click(loginButton)
  .click(Selector('a').withText(prop1.address))
  .expect(Selector('button').withText('listing').exists).notOk('Could see an activation button for the property when logged in as a user that did not list the property')
});

fixture`Apartment Interaction`
  .page`http://localhost:4567/`
  .beforeEach(async t => {
    await t
      .click(Selector('a').withText(prop1.address));
  });

test('Can see and use a link to login to like the apartment when not logged in', async t => {
  const likeLink = Selector('a').withText('Login to LIKE this apartment');
  await t
    .expect(likeLink.exists).ok('Could not find the "Login to LIKE this apartment" link')
    .expect(likeLink.innerText).eql('Login to LIKE this apartment', 'The "Login to LIKE this apartment" has extra words')
    .click(likeLink)
    .typeText(loginEmail, monica.email)
    .typeText(loginPassword, monica.password)
    .click(loginButton)
    .expect(Selector('button').withText('Like this apartment').exists).ok('Could not see a "Like this apartment" button after loggin in');
});

test('Can use the "Like this apartment" button to like the apartment', async t => {
  await t
    .navigateTo('/login')
    .typeText(loginEmail, monica.email)
    .typeText(loginPassword, monica.password)
    .click(loginButton)
    .click(Selector('a').withText(prop1.address))
    .click(Selector('button').withText('Like this apartment'))
    .expect(Selector('button').withText('Like this apartment').exists).notOk('Could still see a "Like this apartment" button even after clicking it');
});
