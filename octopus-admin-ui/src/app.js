const e = React.createElement;

function App() {
  const [urls, setUrls] = React.useState([]);
  const [longUrl, setLongUrl] = React.useState('');
  const [description, setDescription] = React.useState('');
  const load = () => {
    axios.get('/api/url').then(res => setUrls(res.data));
  };
  const create = () => {
    axios.post('/api/url', {longUrl, description}).then(load);
  };
  React.useEffect(load, []);
  return e('div', null,
    e('h1', null, 'Octopus Admin'),
    e('div', null,
      e('input', {value: longUrl, onChange: ev => setLongUrl(ev.target.value), placeholder:'Long URL'}),
      e('input', {value: description, onChange: ev => setDescription(ev.target.value), placeholder:'Description'}),
      e('button', {onClick:create}, 'Create')
    ),
    e('ul', null, urls.map(u => e('li', {key: u.id}, u.shortUrl)))
  );
}

ReactDOM.render(e(App), document.getElementById('root'));
