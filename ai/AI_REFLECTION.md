# AI Reflection

I used Kiro, an AI-powered IDE built by AWS. I've been experimenting with it recently and felt this project was a good opportunity to put it through its paces on a real, scoped problem — from planning through to delivery.

Kiro helped me map out an implementation plan before writing a single line of code, which kept the design clean and deliberate. It also wrote the automated tests, covering unit tests for each component as well as a specific tiebreaker test that asserts goal average is used rather than goal difference — the key historical rule the problem is testing for.

Where Kiro struggled was sourcing the match data. I asked it to find and populate the input CSV with real 1974/75 First Division results. It attempted to reconstruct the data by cross-referencing multiple Wikipedia club season pages, but produced duplicates and inconsistencies across two attempts. Rather than let it keep iterating, I pulled the data directly from fbref.com myself and pasted it into the chat. Kiro then converted it to clean, validated CSV immediately. The lesson I took from it: AI tools are strong at transformation and generation, but unreliable when the task requires navigating fragmented, inconsistent sources. Knowing when to step in rather than waiting for the AI to self-correct is part of effective collaboration.
