# Scrum – Additional Requirements (Optional AI)

Based on the project's optional FRs:

FR-09 AI-generated summaries

FR-10 Automatic classification/priority suggestion

FR-11 AI-independent operation

These requirements are optional but assessable as added value.

1️⃣ Objective

Add AI assistance without breaking the base system.

Mandatory condition:

The system must work completely without AI (FR-11).

2️⃣ Product Goal

Implement AI as a decoupled service that:

- [ ] Suggests classification/priority
- [ ] Generates summaries
- [ ] Is optional and can be disabled

3️⃣ AI Backlog

🔹 AI Architecture Design

- [ ] Define AIService interface
- [ ] Design adapter for external LLM
- [ ] Design fallback without AI
- [ ] Define timeout and retry
- [ ] Define AI error handling
- [ ] Define AI feature flag ON/OFF

🔹 FR-10 Classification/Priority Suggestion

- [ ] Endpoint /ai/suggest-classification
- [ ] Send request description to LLM
- [ ] Receive suggested type
- [ ] Receive suggested priority
- [ ] Display suggestion in UI
- [ ] Allow human confirmation
- [ ] Record suggestion history

🔹 FR-09 Request Summary

- [ ] Endpoint /ai/summary
- [ ] Send request history to LLM
- [ ] Generate text summary
- [ ] Display summary in UI
- [ ] Optionally save summary
- [ ] Validate length and format

🔹 FR-11 AI Independence

- [ ] System works without AI API
- [ ] Graceful fallback handling
- [ ] "AI not available" message
- [ ] Tests without AI
- [ ] Tests with mock AI

🔹 Security and Costs

- [ ] Sanitize inputs
- [ ] Limit tokens
- [ ] Control rate limit
- [ ] Hide sensitive data
- [ ] AI usage logging

🔹 AI UX

- [ ] Label suggestions as "AI"
- [ ] Allow editing suggestions
- [ ] Show AI confidence
- [ ] Regenerate summary button

🔹 AI Testing

- [ ] Mock AI tests
- [ ] Fallback tests
- [ ] Prompt security tests
- [ ] Performance tests

4️⃣ AI Sprint Planning (2 weeks suggested)

Week 1 – AI Backend

- [ ] Design AI interface
- [ ] Implement LLM adapter
- [ ] Suggestion endpoint
- [ ] Summary endpoint
- [ ] Mock tests

Week 2 – Frontend + Validation

- [ ] Classification suggestion UI
- [ ] History summary UI
- [ ] Human confirmation
- [ ] Fallback without AI
- [ ] AI demo

5️⃣ AI Definition of Done

- [ ] AI is decoupled
- [ ] System works without AI
- [ ] Suggestions are editable
- [ ] Summary is functional
- [ ] Tests pass
- [ ] Costs are controlled

6️⃣ AI Technical Risks

- [ ] Critical dependency on external API
- [ ] Sensitive data sent
- [ ] Unexpected costs
- [ ] High latency
- [ ] Incorrect AI responses

7️⃣ AI Metrics

- [ ] % suggestions accepted by users
- [ ] AI response time
- [ ] No. of AI errors
- [ ] Cost per request
- [ ] Total AI usage
