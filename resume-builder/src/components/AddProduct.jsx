import React, { useState } from 'react'
import axios from 'axios'

function AddProduct() {
  const [name, setName] = useState('')
  const [price, setPrice] = useState('')
  const [message, setMessage] = useState('')
  const [pdf, setPdf] = useState(null)
  const [generatedResponse, setGeneratedResponse] = useState(null)
  const [loading, setLoading] = useState(false)

  const handlePdfChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      setPdf(e.target.files[0])
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setMessage('')
    setLoading(true)
    try {
      const formData = new FormData()
      formData.append('prodName', name)
      // formData.append('price', parseFloat(price))
      if (pdf) {
        formData.append('pdf', pdf)
      }

      const response = await axios.post('http://localhost:8080/products/multi-part', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      setMessage('Suggestions generated successfully!')
      setName('')
      setPrice('')
      setPdf(null)
      setGeneratedResponse(typeof response.data === 'string' ? response.data : JSON.stringify(response.data))
    } catch (err) {
      setMessage('Error: ' + (err.response?.data?.message || err.message))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="add-product-container">
      <h2>Resume Rocket</h2>
      <form onSubmit={handleSubmit} style={{ maxWidth: 500, margin: '0 auto' }}>
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', fontWeight: 'bold', marginBottom: 6 }}>
            Enter the Job Description:
          </label>
          <textarea
            value={name}
            onChange={e => setName(e.target.value)}
            required
            rows={3}
            style={{
              width: '100%',
              padding: '10px',
              borderRadius: '6px',
              border: '1px solid #ccc',
              fontSize: '1em',
              resize: 'vertical',
              boxSizing: 'border-box'
            }}
          />
        </div>
        <div style={{ marginBottom: '20px' }}>
          <label style={{ display: 'block', fontWeight: 'bold', marginBottom: 6 }}>
            Upload Resume (PDF):
          </label>
          <input
            type="file"
            accept="application/pdf"
            onChange={handlePdfChange}
            style={{
              padding: '6px',
              borderRadius: '6px',
              border: '1px solid #ccc',
              background: '#fff',
              fontSize: '1em'
            }}
          />
          {pdf && <p style={{ marginTop: 8, color: '#1976d2' }}>Selected file: {pdf.name}</p>}
        </div>
        <button
          type="submit"
          style={{
            background: '#1976d2',
            color: '#fff',
            padding: '10px 28px',
            border: 'none',
            borderRadius: '6px',
            fontSize: '1em',
            fontWeight: 'bold',
            cursor: 'pointer',
            transition: 'background 0.2s'
          }}
          onMouseOver={e => (e.target.style.background = '#125ea2')}
          onMouseOut={e => (e.target.style.background = '#1976d2')}
          disabled={loading}
        >
          {loading ? 'Loading...' : 'Suggest'}
        </button>
      </form>
      {message && <p>{message}</p>}
      {generatedResponse && (
        <div
          style={{
            background: '#f4f4f4',
            border: '1px solid #ccc',
            borderRadius: '8px',
            padding: '16px',
            marginTop: '20px',
            fontFamily: 'monospace',
            color: '#333',
            maxWidth: '100%',
            overflowX: 'auto',
            wordBreak: 'break-word',
            boxShadow: '0 2px 8px rgba(0,0,0,0.05)'
          }}
        >
          <h3 style={{ marginTop: 0, color: '#1976d2' }}>Generated Response:</h3>
          <pre
            style={{
              margin: 0,
              background: 'none',
              fontSize: '1em',
              whiteSpace: 'pre-wrap',
              wordBreak: 'break-word',
              overflowX: 'auto'
            }}
          >
            {generatedResponse}
          </pre>
        </div>
      )}
    </div>
  )
}

export default AddProduct